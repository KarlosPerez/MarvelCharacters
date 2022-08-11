package com.karlosprojects.characters_data.remote

import com.karlosprojects.characters_data.dto.CharactersDto
import com.karlosprojects.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApi {

    @GET("v1/public/characters")
    suspend fun getCharacters(
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("ts") ts: String = Constants.ts,
        @Query("hash") hash: String = Constants.hash(),
        @Query("limit") limit: String = Constants.limit
    ): CharactersDto

    @GET("v1/public/characters/{characterId}")
    suspend fun getCharacterDetail(
        @Path("characterId") id: Int,
        @Query("apikey") apiKey: String = Constants.API_KEY,
        @Query("ts") ts: String = Constants.ts,
        @Query("hash") hash: String = Constants.hash()
    ): CharactersDto
}