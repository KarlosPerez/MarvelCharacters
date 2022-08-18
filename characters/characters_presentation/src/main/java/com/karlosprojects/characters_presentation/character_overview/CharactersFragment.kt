package com.karlosprojects.characters_presentation.character_overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.karlosprojects.characters_domain.model.MarvelCharacter
import com.karlosprojects.characters_presentation.databinding.FragmentCharactersBinding
import com.karlosprojects.characters_presentation.extensions.showStringSnackBar
import com.karlosprojects.helpers.ItemClickListener
import com.karlosprojects.utils.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharactersViewModel by viewModels()

    private val listenerCharacter = object : ItemClickListener<MarvelCharacter> {
        override fun onItemClick(item: MarvelCharacter) {
            val action = CharactersFragmentDirections
                .actionCharactersFragmentToCharacterDetailFragment(item.id)
            findNavController().navigate(action)
        }
    }

    private val charactersAdapter = CharactersAdapter(listenerCharacter)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObservers()
        initListeners()
        viewModel.onCharactersEvent(CharacterEvent.OnRequestCharacters)
    }

    private fun initListeners() {
        binding.lyEmptyLanding.btnRetry.setOnClickListener {
            binding.lyEmptyLanding.pbLoading.visibility = View.VISIBLE
            viewModel.onCharactersEvent(CharacterEvent.OnRequestCharacters)
        }
    }

    private fun initObservers() {
        with(binding) {
            viewModel.uiEvent.onEach { event ->
                when (event) {
                    UiEvent.ShowEmptyState -> {
                        lyEmptyLanding.pbLoading.hideIfRequired()
                        lyEmptyLanding.root.visibility = View.VISIBLE
                        rvCharacters.visibility = View.GONE
                        cpCharactersLoading.visibility = View.GONE
                    }
                    is UiEvent.ShowSnackBar -> {
                        root.showStringSnackBar(event.message.asString(requireContext()))
                    }
                    else -> Unit
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

            viewModel.charactersState.onEach { state ->
                if (state.isLoading) {
                    rvCharacters.visibility = View.GONE
                    cpCharactersLoading.visibility = View.VISIBLE
                } else {
                    cpCharactersLoading.visibility = View.GONE
                }
                if (state.characters.isNotEmpty()) {
                    lyEmptyLanding.pbLoading.hideIfRequired()
                    lyEmptyLanding.root.visibility = View.GONE
                    cpCharactersLoading.visibility = View.GONE
                    rvCharacters.visibility = View.VISIBLE
                    charactersAdapter.submitList(state.characters)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun initRecyclerView() {
        binding.rvCharacters.adapter = charactersAdapter
    }

    private fun ProgressBar.hideIfRequired() {
        if (this.isVisible) {
            binding.lyEmptyLanding.pbLoading.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvCharacters.adapter = null
        _binding = null
    }

}
