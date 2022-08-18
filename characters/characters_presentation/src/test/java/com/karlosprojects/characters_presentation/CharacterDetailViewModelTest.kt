package com.karlosprojects.characters_presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.karlosprojects.characters_domain.model.CharacterDetail
import com.karlosprojects.characters_domain.usecases.GetCharacterDetail
import com.karlosprojects.characters_presentation.character_detail.CharacterDetailEvent
import com.karlosprojects.characters_presentation.character_detail.CharacterDetailUiState
import com.karlosprojects.characters_presentation.character_detail.CharacterDetailViewModel
import com.karlosprojects.characters_presentation.utils.MainCoroutineRule
import com.karlosprojects.utils.UiEvent
import com.karlosprojects.utils.UiText
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class CharacterDetailViewModelTest {

    private lateinit var viewModel: CharacterDetailViewModel

    private val getCharacterDetailUC = mockk<GetCharacterDetail>()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        viewModel = CharacterDetailViewModel(getCharacterDetailUC)
    }

    @Test
    fun getCharacterDetail() = runTest {
        /** preparation */
        val slot = slot<Int>()
        val characterDetail = CharacterDetail(
            id = 1001,
            name = "test",
            modified = "modified",
            description = "description",
            thumbnail = "thumbnail",
            comicAvailable = 1,
            seriesAvailable = 1,
            storiesAvailable = 1
        )
        coEvery { getCharacterDetailUC.invoke(capture(slot)) } returns Result.success(
            characterDetail
        )
        val result = arrayListOf<CharacterDetailUiState>()
        val uiEvent = arrayListOf<UiEvent>()

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.characterDetailState.toList(result)
        }

        val jobEvent = launch(UnconfinedTestDispatcher()) {
            viewModel.uiEvent.toList(uiEvent)
        }

        /** execution */
        viewModel.onCharactersEvent(CharacterDetailEvent.OnRequestCharacterDetail(1001))

        /** verification */
        Assert.assertEquals(1001, slot.captured)
        Assert.assertNotNull(result)
        Assert.assertFalse(result.first().isLoading)
        Assert.assertTrue(result[1].isLoading)
        Assert.assertFalse(result[2].isLoading)
        Assert.assertNotNull(result[2].character)
        Assert.assertEquals(1001, result[2].character?.id)
        Assert.assertEquals("test", result[2].character?.name)
        Assert.assertEquals(0, uiEvent.count())
        coVerify(exactly = 1) { getCharacterDetailUC.invoke(1001) }
        confirmVerified(getCharacterDetailUC)

        job.cancel()
        jobEvent.cancel()
    }

    @Test
    fun `getCharacterDetail Failure should return a snackBar event with default exception`() =
        runTest {
            /** preparation */
            val slot = slot<Int>()
            val result = arrayListOf<CharacterDetailUiState>()
            val resultEvent = arrayListOf<UiEvent>()

            coEvery { getCharacterDetailUC.invoke(capture(slot)) } returns Result.failure(
                Exception("error detail")
            )
            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.characterDetailState.toList(result)
            }

            val jobEffect = launch(UnconfinedTestDispatcher()) {
                viewModel.uiEvent.toList(resultEvent)
            }

            /** execution */
            viewModel.onCharactersEvent(CharacterDetailEvent.OnRequestCharacterDetail(1001))

            /** verification */
            Assert.assertNotNull(result)
            Assert.assertNotNull(resultEvent)
            Assert.assertEquals(1, resultEvent.count())
            Assert.assertEquals(3, result.count())
            Assert.assertFalse(result.first().isLoading)
            Assert.assertNull(result.first().character)
            Assert.assertTrue(result[1].isLoading)
            Assert.assertNull(result[1].character)
            Assert.assertFalse(result[2].isLoading)
            Assert.assertNull(result[2].character)
            Assert.assertTrue(resultEvent.first() is UiEvent.ShowSnackBar)
            Assert.assertEquals(
                R.string.characters_characters_other_error,
                ((resultEvent.first() as UiEvent.ShowSnackBar).message as UiText.StringResource).resId
            )
            Assert.assertEquals(1001, slot.captured)
            coVerify(exactly = 1) { getCharacterDetailUC.invoke(slot.captured) }
            job.cancel()
            jobEffect.cancel()
        }

    @Test
    fun `getCharacterDetail Network Failure should return snackBar event UnknownHostException`() =
        runTest {
            /** preparation */
            val slot = slot<Int>()
            val result = arrayListOf<CharacterDetailUiState>()
            val resultEvent = arrayListOf<UiEvent>()

            coEvery { getCharacterDetailUC.invoke(capture(slot)) } returns Result.failure(
                UnknownHostException("host exception")
            )
            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.characterDetailState.toList(result)
            }

            val jobEvent = launch(UnconfinedTestDispatcher()) {
                viewModel.uiEvent.toList(resultEvent)
            }

            /** execution */
            viewModel.onCharactersEvent(CharacterDetailEvent.OnRequestCharacterDetail(1001))

            /** verification */
            Assert.assertNotNull(result)
            Assert.assertNotNull(resultEvent)
            Assert.assertEquals(1, resultEvent.count())
            Assert.assertEquals(3, result.count())
            Assert.assertFalse(result.first().isLoading)
            Assert.assertNull(result.first().character)
            Assert.assertTrue(result[1].isLoading)
            Assert.assertNull(result[1].character)
            Assert.assertFalse(result[2].isLoading)
            Assert.assertNull(result[2].character)
            Assert.assertTrue(resultEvent.first() is UiEvent.ShowSnackBar)
            Assert.assertEquals(
                R.string.character_detail_network_error,
                ((resultEvent.first() as UiEvent.ShowSnackBar).message as UiText.StringResource).resId
            )
            Assert.assertEquals(1001, slot.captured)
            coVerify(exactly = 1) { getCharacterDetailUC.invoke(slot.captured) }
            job.cancel()
            jobEvent.cancel()
        }
}