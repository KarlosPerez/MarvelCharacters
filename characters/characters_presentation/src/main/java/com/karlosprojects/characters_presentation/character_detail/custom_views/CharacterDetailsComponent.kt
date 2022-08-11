package com.karlosprojects.characters_presentation.character_detail.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.karlosprojects.characters_domain.model.CharacterDetail
import com.karlosprojects.characters_presentation.R
import com.karlosprojects.characters_presentation.databinding.LayoutCharacterDetailsBinding

class CharacterDetailsComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutCharacterDetailsBinding.inflate(
        LayoutInflater.from(context),
        this
    )

    fun setCharacterInfo(character: CharacterDetail) {
        with(binding) {
            Glide
                .with(context)
                .load(character.thumbnail)
                .into(itemImvCharacter)
            characterDetailTxtId.text = character.id.toString()
            characterDetailTxtName.text = character.name
            characterDetailTxtDescription.text =
                character.description.takeUnless { it.isEmpty() }
                    ?: context.getString(R.string.character_detail_no_description)
            characterDetailTxtModified.text = character.modified
        }
    }

}