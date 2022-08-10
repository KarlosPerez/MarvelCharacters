package com.karlosprojects.characters_domain.usecases

import com.karlosprojects.characters_domain.model.MarvelCharacter
import com.karlosprojects.characters_domain.repository.CharactersRepository

class GetCharacters(
    private val repository: CharactersRepository
) {

    suspend operator fun invoke(): Result<List<MarvelCharacter>> {
        return repository.getCharacters()
    }
}