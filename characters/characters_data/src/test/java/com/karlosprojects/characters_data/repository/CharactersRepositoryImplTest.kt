package com.karlosprojects.characters_data.repository

import com.karlosprojects.characters_data.remote.MarvelApi
import com.karlosprojects.characters_data.remote.invalidCharacterResponse
import com.karlosprojects.characters_data.remote.validCharacterResponse
import com.karlosprojects.characters_domain.repository.CharactersRepository
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CharactersRepositoryImplTest {

    private lateinit var repository: CharactersRepository
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var api: MarvelApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        okHttpClient = OkHttpClient.Builder()
            .writeTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build()
        api = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(MarvelApi::class.java)
        repository = CharactersRepositoryImpl(
            marvelApi = api
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getCharactersSuccess() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validCharacterResponse)
        )
        val result = repository.getCharacters()

        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun getCharactersFailure() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(403)
                .setBody(invalidCharacterResponse)
        )
        val result = repository.getCharacters()

        Assert.assertTrue(result.isFailure)
    }

}