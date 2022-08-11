package com.karlosprojects.characters_domain.usecases

import com.karlosprojects.characters_domain.model.CharacterDetail
import com.karlosprojects.characters_domain.repository.CharacterDetailRepository

class GetCharacterDetail(
    private val repository: CharacterDetailRepository
) {

    suspend operator fun invoke(id: Int): Result<CharacterDetail> {
        return repository.getCharacterDetail(id)
    }
}