package com.karlosprojects.characters_presentation.character_detail

import com.karlosprojects.characters_domain.model.CharacterDetail

data class CharacterDetailUiState(
    val isLoading: Boolean = false,
    val character: CharacterDetail? = null
)
