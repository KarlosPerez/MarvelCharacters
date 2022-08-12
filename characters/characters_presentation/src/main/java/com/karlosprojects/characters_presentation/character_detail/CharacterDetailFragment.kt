package com.karlosprojects.characters_presentation.character_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.karlosprojects.characters_presentation.R
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
        initListeners()
        viewModel.onCharactersEvent(OnRequestCharacterDetail(characterIdArg.characterId))

    }

    private fun initListeners() {
        binding.tbCharacter.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initObservers() {
        with(binding) {
            viewModel.uiEvent.onEach { event ->
                if (event is UiEvent.ShowSnackBar) {
                    binding.root.showStringSnackBar(event.message.asString(requireContext()))
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            viewModel.characterDetailState.onEach { character ->
                if (character.isLoading) {
                    cpCharacterLoading.visibility = View.VISIBLE
                } else {
                    cpCharacterLoading.visibility = View.GONE
                    character.character?.let {
                        cvCharacterDetail.setCharacterInfo(character.character)
                        cvSeriesAvailable.setAvailableInfo(
                            character.character.seriesAvailable.toString(),
                            getString(R.string.character_detail_series_title)
                        )
                        cvComicsAvailable.setAvailableInfo(
                            character.character.comicAvailable.toString(),
                            getString(R.string.character_detail_comics_title)
                        )
                        cvStoriesAvailable.setAvailableInfo(
                            character.character.storiesAvailable.toString(),
                            getString(R.string.character_detail_stories_title)
                        )
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}