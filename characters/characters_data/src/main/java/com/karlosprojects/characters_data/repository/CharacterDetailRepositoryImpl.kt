package com.karlosprojects.characters_data.repository

import com.karlosprojects.characters_data.mappers.toCharacterDetailDomain
import com.karlosprojects.characters_domain.model.CharacterDetail
import com.karlosprojects.characters_domain.repository.CharacterDetailRepository
import com.karlosprojects.core_network.api.MarvelApi

class CharacterDetailRepositoryImpl(
    private val marvelApi: MarvelApi
) : CharacterDetailRepository {

    override suspend fun getCharacterDetail(id: Int): Result<CharacterDetail> {
        return try {
            val result = marvelApi.getCharacterDetail(id = id)
            Result.success(result.data.results.first().toCharacterDetailDomain())
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}