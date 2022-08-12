package com.karlosprojects.characters_presentation.character_detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.karlosprojects.characters_domain.model.CharacterDetail
import com.karlosprojects.characters_domain.usecases.GetCharacterDetail
import com.karlosprojects.characters_presentation.utils.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
        var character = CharacterDetailState()
        val detail = CharacterDetail(
            id = 1001,
            name = "name",
            modified = "modified",
            description = "description",
            thumbnail = "thumbnail",
            comicAvailable = 1,
            seriesAvailable = 1,
            storiesAvailable = 1
        )
        coEvery { getCharacterDetailUC.invoke(any()) } answers { Result.success(detail) }

        /** execution */
        viewModel.onCharactersEvent(CharacterDetailEvent.OnRequestCharacterDetail(1001))
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.characterDetailState.collect {
                character = CharacterDetailState(
                    isLoading = false,
                    character = it.character
                )
            }
        }

        /** verification */
        coVerify { getCharacterDetailUC.invoke(any()) }
        Assert.assertEquals(detail.thumbnail, character.character?.thumbnail)
        confirmVerified(getCharacterDetailUC)
        job.cancel()
    }

    @Test
    fun getCharacterDetailFailure() = runTest {
        /** preparation */
        val exception = Throwable()
        var character = CharacterDetailState()
        coEvery { getCharacterDetailUC.invoke(any()) } answers { Result.failure(exception) }

        /** execution */
        viewModel.onCharactersEvent(CharacterDetailEvent.OnRequestCharacterDetail(1001))
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.characterDetailState.collect {
                character = CharacterDetailState(
                    isLoading = false
                )
            }
        }

        /** verification */
        coVerify { getCharacterDetailUC.invoke(any()) }
        Assert.assertTrue(character.character == null)
        confirmVerified(getCharacterDetailUC)
        job.cancel()
    }
}