package com.karlosprojects.characters_presentation.character_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karlosprojects.characters_domain.usecases.GetCharacterDetail
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
class CharacterDetailViewModel @Inject constructor(
    private val getCharacterDetailUC: GetCharacterDetail
) : ViewModel() {

    private val _characterDetailState = MutableStateFlow(CharacterDetailUiState())
    val characterDetailState: StateFlow<CharacterDetailUiState> = _characterDetailState

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onCharactersEvent(event: CharacterDetailEvent) {
        when (event) {
            is CharacterDetailEvent.OnRequestCharacterDetail -> {
                _characterDetailState.value = _characterDetailState.value.copy(isLoading = true)
                getCharacterDetail(event.characterId)
            }
        }
    }

    private fun getCharacterDetail(characterId: Int) = viewModelScope.launch {
        getCharacterDetailUC(id = characterId)
            .onSuccess { character ->
                _characterDetailState.value = _characterDetailState.value.copy(
                    isLoading = false,
                    character = character
                )
            }
            .onFailure { error ->
                _characterDetailState.value = _characterDetailState.value.copy(
                    isLoading = false
                )
                if (error is UnknownHostException) {
                    _uiEvent.send(
                        UiEvent.ShowSnackBar(
                            UiText.StringResource(R.string.character_detail_network_error)
                        )
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