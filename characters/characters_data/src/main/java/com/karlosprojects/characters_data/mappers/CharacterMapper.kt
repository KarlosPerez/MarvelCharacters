package com.karlosprojects.characters_data.mappers

import com.karlosprojects.characters_data.dto.Results
import com.karlosprojects.characters_data.dto.Thumbnail
import com.karlosprojects.characters_domain.model.CharacterDetail
import com.karlosprojects.characters_domain.model.MarvelCharacter

fun Results.toCharacterDomain(): MarvelCharacter {

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

fun Results.toCharacterDetailDomain(): CharacterDetail {

    return CharacterDetail(
        id = id,
        name = name,
        description = description,
        thumbnail = thumbnail.getThumbnail(),
        comicAvailable = comics.available,
        seriesAvailable = series.available,
        storiesAvailable = stories.available
    )
}

internal fun Thumbnail.getThumbnail() : String = "$path.$extension".let {
    return it.replace("http", "https")
}