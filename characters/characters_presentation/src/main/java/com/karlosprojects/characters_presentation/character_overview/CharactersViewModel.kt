package com.karlosprojects.characters_presentation.character_overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karlosprojects.characters_domain.usecases.GetCharacters
import com.karlosprojects.characters_presentation.R
import com.karlosprojects.utils.UiEvent
import com.karlosprojects.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUC: GetCharacters
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _charactersState = MutableStateFlow(CharacterOverviewUiState())
    val charactersState: StateFlow<CharacterOverviewUiState> = _charactersState

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
                    isLoading = false
                )
                if (error is UnknownHostException) {
                    _uiEvent.send(
                        UiEvent.ShowEmptyState
                    )
                } else {
                    _uiEvent.send(
                        UiEvent.ShowSnackBar(
                            UiText.StringResource(R.string.characters_characters_other_error)
                        )
                    )
                }
            }
    }

}