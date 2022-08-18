package com.karlosprojects.characters_domain.usecases

import com.karlosprojects.characters_domain.model.MarvelCharacter
import com.karlosprojects.characters_domain.repository.CharactersRepository
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

class GetCharactersTest {

    private val repository = mockk<CharactersRepository>(relaxUnitFun = true)

    private lateinit var getCharactersUC: GetCharacters

    @Before
    fun setUp() {
        getCharactersUC = GetCharacters(repository)
    }

    @After
    fun tearDown() {
        confirmVerified(repository)
    }

    @Test
    fun getCharactersDetails() = runBlocking {
        /** preparation */
        val characters = mockk<MarvelCharacter>(relaxed = true)
        val characterList = listOf(characters)
        coEvery { repository.getCharacters() } returns Result.success(characterList)

        /** execution */
        val test = getCharactersUC()

        /** verification */
        assertTrue(test.isSuccess)
        coVerify { repository.getCharacters() }
        confirmVerified(characters)
    }

    @Test
    fun getCharactersFailure() = runBlocking {
        /** preparation */
        val exception = mockk<UnknownHostException>()
        coEvery { repository.getCharacters() } returns Result.failure(exception)

        /** execution */
        val test = getCharactersUC()

        /** verification */
        assertTrue(test.isFailure)
        coVerify { repository.getCharacters() }
    }
}