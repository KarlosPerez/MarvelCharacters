package com.karlosprojects.characters_data.repository

import com.karlosprojects.characters_domain.repository.CharacterDetailRepository
import com.karlosprojects.core_model.dto.*
import com.karlosprojects.core_network.api.MarvelApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CharacterDetailRepositoryImplTest {

    private lateinit var repository: CharacterDetailRepository
    private val api = mockk<MarvelApi>(relaxed = true)

    @Before
    fun setUp() {
        repository = CharacterDetailRepositoryImpl(
            marvelApi = api
        )
    }

    @After
    fun tearDown() {
        confirmVerified(api)
    }

    @Test
    fun getCharacterDetailSuccess() = runBlocking {
        /** preparation */
        val responseDto = CharactersDto(
            code = 200,
            status = "Ok",
            copyright = "copyright",
            data = Data(
                listOf(
                    Results(
                        id = 1,
                        description = "description",
                        modified = "2013-09-18T15:54:04-0400",
                        name = "name",
                        resourceURI = String(),
                        series = Series(2),
                        thumbnail = Thumbnail("path", "extension"),
                        comics = Comics(1, listOf()),
                        stories = Stories(2),
                        urls = listOf()
                    )
                )
            )
        )
        coEvery {
            api.getCharacterDetail(1017100)
        } answers { responseDto }

        /** execution */
        val test = repository.getCharacterDetail(1017100)

        /** verification */
        Assert.assertTrue(test.isSuccess)
        coVerify { api.getCharacterDetail(1017100) }
    }
}