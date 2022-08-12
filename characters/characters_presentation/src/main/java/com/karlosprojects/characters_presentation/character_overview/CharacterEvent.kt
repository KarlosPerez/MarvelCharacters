package com.karlosprojects.characters_presentation.character_overview

sealed class CharacterEvent {
    object OnRequestCharacters : CharacterEvent()
}
