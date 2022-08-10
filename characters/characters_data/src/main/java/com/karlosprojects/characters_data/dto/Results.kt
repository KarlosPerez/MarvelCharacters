package com.karlosprojects.characters_data.dto

data class Results(
    val id: Int,
    val description: String,
    val modified: String,
    val name: String,
    val resourceURI: String,
    val series: Series,
    val thumbnail: Thumbnail,
    val comics: List<Comics>,
    val urls: List<Urls>
)