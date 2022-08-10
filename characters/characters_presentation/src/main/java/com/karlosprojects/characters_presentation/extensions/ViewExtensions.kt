package com.karlosprojects.characters_presentation.extensions

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(
    @StringRes messageRes: Int,
    length: Int = Snackbar.LENGTH_LONG
) {
    val snackBar = Snackbar.make(this, resources.getString(messageRes), length)
    snackBar.show()
}

fun View.showStringSnackBar(
    messageRes: String,
    length: Int = Snackbar.LENGTH_LONG,
) {
    val snackBar = Snackbar.make(this, messageRes, length)
    snackBar.show()
}

fun AppCompatImageView.load(url: String, context: Context) {
    Glide
        .with(context)
        .load(url)
        .circleCrop()
        .into(this)
}