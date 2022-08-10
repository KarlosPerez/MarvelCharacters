package com.karlosprojects.characters_presentation.character_overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.karlosprojects.characters_domain.model.MarvelCharacter
import com.karlosprojects.characters_presentation.databinding.ItemCharacterBinding
import com.karlosprojects.characters_presentation.extensions.load
import com.karlosprojects.helpers.ItemClickListener

class CharactersAdapter(
    private val itemListener: ItemClickListener<MarvelCharacter>
) : ListAdapter<MarvelCharacter, CharactersAdapter.CharacterViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding =
            ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class CharacterViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val character = getItem(position)
                        itemListener.onItemClick(character)
                    }
                }
            }
        }

        fun bind(character: MarvelCharacter) {
            with(binding) {
                itemTxtCharacterName.text = character.name
                itemImvCharacter.load(character.thumbnail, root.context)
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<MarvelCharacter>() {

        override fun areItemsTheSame(oldItem: MarvelCharacter, newItem: MarvelCharacter) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MarvelCharacter, newItem: MarvelCharacter) =
            oldItem == newItem

    }

}