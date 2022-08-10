package com.karlosprojects.characters_domain.model

data class MarvelCharacter(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: String,
    val comicAvailable: Int,
    val seriesAvailable: Int,
    val storiesAvailable : Int
)
