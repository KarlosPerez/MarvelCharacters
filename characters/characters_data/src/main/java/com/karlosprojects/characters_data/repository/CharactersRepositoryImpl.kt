package com.karlosprojects.characters_data.repository

import com.karlosprojects.characters_data.mappers.toCharacterDomain
import com.karlosprojects.characters_domain.model.MarvelCharacter
import com.karlosprojects.characters_domain.repository.CharactersRepository
import com.karlosprojects.core_network.api.MarvelApi

class CharactersRepositoryImpl(
    private val marvelApi: MarvelApi
) : CharactersRepository {

    override suspend fun getCharacters(): Result<List<MarvelCharacter>> {
        return try {
            val result = marvelApi.getCharacters()
            Result.success(result.data.results.map {
                it.toCharacterDomain()
            })
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }


}