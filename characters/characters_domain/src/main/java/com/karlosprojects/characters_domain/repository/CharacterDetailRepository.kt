package com.karlosprojects.characters_domain.repository

import com.karlosprojects.characters_domain.model.CharacterDetail

interface CharacterDetailRepository {

    suspend fun getCharacterDetail(id: Int): Result<CharacterDetail>
}