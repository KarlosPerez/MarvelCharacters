package com.karlosprojects.characters_presentation.character_detail.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.karlosprojects.characters_presentation.databinding.LayoutItemAvailableBinding

class AvailableInfoComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutItemAvailableBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun setAvailableInfo(available: String, name: String) {
        with(binding) {
            characterDetailTxtAvailableName.text = name
            characterDetailTxtAvailableQuantity.text = available
        }
    }

}