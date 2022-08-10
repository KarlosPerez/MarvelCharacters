package com.karlosprojects.characters_presentation.character_overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
            binding.root.showStringSnackBar(item.name)
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
            viewModel.onCharactersEvent(CharacterEvent.OnRequestCharacters)
        }
    }

    private fun initObservers() {
        viewModel.uiEvent.onEach { event ->
            when (event) {
                UiEvent.ShowEmptyState -> {
                    binding.lyEmptyLanding.root.visibility = View.VISIBLE
                    binding.cpCharactersLoading.visibility = View.GONE
                }
                is UiEvent.ShowSnackBar -> {
                    binding.root.showStringSnackBar(event.message.asString(requireContext()))
                }
                else -> Unit
            }
        }
        viewModel.charactersState.onEach { state ->
            when {
                state.isLoading -> {
                    binding.cpCharactersLoading.visibility = View.VISIBLE
                }
                state.characters.isNotEmpty() -> {
                    binding.lyEmptyLanding.root.visibility = View.GONE
                    binding.cpCharactersLoading.visibility = View.GONE
                    charactersAdapter.submitList(state.characters)
                }
                state.error.isNotEmpty() -> {
                    binding.root.showStringSnackBar(state.error)
                }
            }

        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initRecyclerView() {
        binding.rvCharacters.adapter = charactersAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvCharacters.adapter = null
        _binding = null
    }

}