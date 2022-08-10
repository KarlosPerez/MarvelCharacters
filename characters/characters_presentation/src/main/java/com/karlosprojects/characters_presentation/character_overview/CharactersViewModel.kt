package com.karlosprojects.characters_presentation.character_overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karlosprojects.characters_domain.usecases.GetCharacters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUC: GetCharacters
) : ViewModel() {

    private val _charactersState = MutableStateFlow(CharacterOverviewState())
    val charactersState: StateFlow<CharacterOverviewState> = _charactersState

    fun onCharactersEvent(event: CharacterEvent) {
        when (event) {
            is CharacterEvent.OnRequestCharacters -> {
                _charactersState.value = _charactersState.value.copy(isLoading = true)
                getCharacters()
            }
        }
    }

    private fun getCharacters() = viewModelScope.launch {
        getCharactersUC()
            .onSuccess { characters ->
                _charactersState.value = _charactersState.value.copy(
                    isLoading = false,
                    characters = characters
                )
            }
            .onFailure { error ->
                _charactersState.value = _charactersState.value.copy(
                    isLoading = false,
                    error = error.message.toString()
                )
            }
    }

}