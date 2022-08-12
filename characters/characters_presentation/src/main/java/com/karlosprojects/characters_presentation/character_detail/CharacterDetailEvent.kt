package com.karlosprojects.characters_presentation.character_detail

sealed class CharacterDetailEvent {
    data class OnRequestCharacterDetail(val characterId: Int) : CharacterDetailEvent()
}
