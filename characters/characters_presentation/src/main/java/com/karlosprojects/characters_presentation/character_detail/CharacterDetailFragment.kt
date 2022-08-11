package com.karlosprojects.characters_presentation.character_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.karlosprojects.characters_presentation.character_detail.CharacterDetailEvent.OnRequestCharacterDetail
import com.karlosprojects.characters_presentation.databinding.FragmentCharacterDetailBinding
import com.karlosprojects.characters_presentation.extensions.showStringSnackBar
import com.karlosprojects.utils.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CharacterDetailFragment : Fragment() {

    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterDetailViewModel by viewModels()
    private val characterIdArg: CharacterDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel.onCharactersEvent(OnRequestCharacterDetail(characterIdArg.characterId))

    }

    private fun initObservers() {
        with(binding) {
            viewModel.uiEvent.onEach { event ->
                if (event is UiEvent.ShowSnackBar) {
                    binding.root.showStringSnackBar(event.message.asString(requireContext()))
                }
            }
            viewModel.characterDetailState.onEach { character ->
                if (character.isLoading) {
                    cpCharacterLoading.visibility = View.VISIBLE
                } else {
                    cpCharacterLoading.visibility = View.GONE
                    character.character?.let {
                        characterDetailTxtName.text = it.name
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

}