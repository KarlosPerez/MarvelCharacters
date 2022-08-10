package com.karlosprojects.characters_presentation.character_overview

import com.karlosprojects.characters_domain.model.MarvelCharacter

data class CharacterOverviewState(
    val isLoading: Boolean = false,
    val characters: List<MarvelCharacter> = emptyList(),
    val error: String = String()
)
