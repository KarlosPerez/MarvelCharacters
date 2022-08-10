package com.karlosprojects.characters_data.remote

import com.karlosprojects.characters_data.dto.CharactersDto
import com.karlosprojects.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApi {

    @GET("v1/public/characters")
    suspend fun getCharacters(
        @Query("apiKey") apiKey: String = Constants.API_KEY,
        @Query("ts") ts: String = Constants.ts,
        @Query("hash") hash: String = Constants.hash(),
        @Query("offset") offset: Int = 0
    ): CharactersDto
}