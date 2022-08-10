package com.karlosprojects.characters_data.mappers

import com.karlosprojects.characters_data.dto.Results
import com.karlosprojects.characters_data.dto.Thumbnail
import com.karlosprojects.characters_domain.model.MarvelCharacter

fun Results.toCharacterDomain(): MarvelCharacter {

    fun Thumbnail.getThumbnail() : String = "$path.$extension".let {
        return it.replace("http", "https")
    }

    return MarvelCharacter(
        id = id,
        name = name,
        description = description,
        thumbnail = thumbnail.getThumbnail(),
        comicAvailable = comics.available,
        seriesAvailable = series.available,
        storiesAvailable = stories.available
    )
}