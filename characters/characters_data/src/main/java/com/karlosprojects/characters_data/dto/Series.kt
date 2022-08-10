package com.karlosprojects.characters_data.dto

data class Series(
    val available: Int,
    val collectionURI: String,
    val items: List<Items>
)
