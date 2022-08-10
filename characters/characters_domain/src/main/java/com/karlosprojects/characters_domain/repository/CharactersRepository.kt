package com.karlosprojects.characters_domain.repository

import com.karlosprojects.characters_domain.model.MarvelCharacter

interface CharactersRepository {

    suspend fun getCharacters(): Result<List<MarvelCharacter>>
}