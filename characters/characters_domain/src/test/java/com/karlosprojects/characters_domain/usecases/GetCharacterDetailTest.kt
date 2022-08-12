package com.karlosprojects.characters_domain.usecases

import com.karlosprojects.characters_domain.model.CharacterDetail
import com.karlosprojects.characters_domain.repository.CharacterDetailRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class GetCharacterDetailTest {

    private val repository = mockk<CharacterDetailRepository>(relaxUnitFun = true)

    private lateinit var getCharacterDetailUC: GetCharacterDetail

    @Before
    fun setUp() {
        getCharacterDetailUC = GetCharacterDetail(repository)
    }

    @After
    fun tearDown() {
        confirmVerified(repository)
    }

    @Test
    fun getCharacterDetails() = runBlocking {
        /** preparation */
        val detail = mockk<CharacterDetail>(relaxed = true)
        coEvery { repository.getCharacterDetail(any()) } returns Result.success(detail)

        /** execution */
        val test = getCharacterDetailUC(1001)

        /** verification */
        assertTrue(test.isSuccess)
        coVerify { repository.getCharacterDetail(any()) }
        confirmVerified(detail)
    }

    @Test
    fun getCharacterFailure() = runBlocking {
        /** preparation */
        val exception = mockk<UnknownHostException>()
        coEvery { repository.getCharacterDetail(any()) } returns Result.failure(exception)

        /** execution */
        val test = getCharacterDetailUC(1001)

        /** verification */
        assertTrue(test.isFailure)
        coVerify { repository.getCharacterDetail(any()) }
    }
}