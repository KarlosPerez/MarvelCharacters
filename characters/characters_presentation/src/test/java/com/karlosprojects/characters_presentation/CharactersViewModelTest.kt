package com.karlosprojects.characters_presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.karlosprojects.characters_domain.model.MarvelCharacter
import com.karlosprojects.characters_domain.usecases.GetCharacters
import com.karlosprojects.characters_presentation.character_overview.CharacterEvent
import com.karlosprojects.characters_presentation.character_overview.CharacterOverviewUiState
import com.karlosprojects.characters_presentation.character_overview.CharactersViewModel
import com.karlosprojects.characters_presentation.utils.MainCoroutineRule
import com.karlosprojects.utils.UiEvent
import com.karlosprojects.utils.UiText
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class CharactersViewModelTest {

    private lateinit var viewModel: CharactersViewModel

    private val getCharactersUC = mockk<GetCharacters>()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        viewModel = CharactersViewModel(getCharactersUC)
    }

    @Test
    fun getCharacters() = runTest {
        /** preparation */
        val characters = listOf(
            MarvelCharacter(
                id = 1001,
                name = "test",
                description = "description",
                thumbnail = "thumbnail",
                comicAvailable = 1,
                seriesAvailable = 1,
                storiesAvailable = 1
            )
        )
        coEvery { getCharactersUC.invoke() } returns Result.success(characters)
        val result = arrayListOf<CharacterOverviewUiState>()
        val uiEvent = arrayListOf<UiEvent>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.charactersState.toList(result)
        }

        val jobEvent = launch(UnconfinedTestDispatcher()) {
            viewModel.uiEvent.toList(uiEvent)
        }

        /** execution */
        viewModel.onCharactersEvent(CharacterEvent.OnRequestCharacters)

        /** verification */
        assertNotNull(result)
        assertFalse(result.first().isLoading)
        assertTrue(result[1].isLoading)
        assertFalse(result[2].isLoading)
        assertNotNull(result[2].characters)
        assertEquals(1001, result[2].characters[0].id)
        assertEquals("test", result[2].characters[0].name)
        assertEquals(0, uiEvent.count())
        coVerify(exactly = 1) { getCharactersUC.invoke() }
        confirmVerified(getCharactersUC)

        job.cancel()
        jobEvent.cancel()
    }

    @Test
    fun `getCharacters Failure should return a snackBar event with default exception`() =
        runTest {
            /** preparation */
            val result = arrayListOf<CharacterOverviewUiState>()
            val resultEvent = arrayListOf<UiEvent>()

            coEvery { getCharactersUC.invoke() } returns Result.failure(
                Exception("error detail")
            )
            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.charactersState.toList(result)
            }

            val jobEffect = launch(UnconfinedTestDispatcher()) {
                viewModel.uiEvent.toList(resultEvent)
            }

            /** execution */
            viewModel.onCharactersEvent(CharacterEvent.OnRequestCharacters)

            /** verification */
            assertNotNull(result)
            assertNotNull(resultEvent)
            assertEquals(1, resultEvent.count())
            assertEquals(3, result.count())
            assertFalse(result.first().isLoading)
            assertTrue(result.first().characters.isEmpty())
            assertTrue(result[1].isLoading)
            assertTrue(result[1].characters.isEmpty())
            assertFalse(result[2].isLoading)
            assertTrue(result[2].characters.isEmpty())
            assertTrue(resultEvent.first() is UiEvent.ShowSnackBar)
            assertEquals(
                R.string.characters_characters_other_error,
                ((resultEvent.first() as UiEvent.ShowSnackBar).message as UiText.StringResource).resId
            )
            coVerify(exactly = 1) { getCharactersUC.invoke() }

            job.cancel()
            jobEffect.cancel()
        }

    @Test
    fun `getCharacters Network Failure should return ShowEmptyState`() =
        runTest {
            /** preparation */
            val result = arrayListOf<CharacterOverviewUiState>()
            val resultEvent = arrayListOf<UiEvent>()

            coEvery { getCharactersUC.invoke() } returns Result.failure(
                UnknownHostException("host exception")
            )
            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.charactersState.toList(result)
            }

            val jobEvent = launch(UnconfinedTestDispatcher()) {
                viewModel.uiEvent.toList(resultEvent)
            }

            /** execution */
            viewModel.onCharactersEvent(CharacterEvent.OnRequestCharacters)

            /** verification */
            assertNotNull(result)
            assertNotNull(resultEvent)
            assertEquals(1, resultEvent.count())
            assertEquals(3, result.count())
            assertFalse(result.first().isLoading)
            assertTrue(result.first().characters.isEmpty())
            assertTrue(result[1].isLoading)
            assertTrue(result[1].characters.isEmpty())
            assertFalse(result[2].isLoading)
            assertTrue(result[2].characters.isEmpty())
            assertTrue(resultEvent.first() is UiEvent.ShowEmptyState)
            coVerify(exactly = 1) { getCharactersUC.invoke() }

            job.cancel()
            jobEvent.cancel()
        }
}