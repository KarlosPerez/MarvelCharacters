package com.karlosprojects.characters_data.mappers

import com.karlosprojects.characters_data.dto.Results
import com.karlosprojects.characters_domain.model.MarvelCharacter

fun Results.toCharacterDomain(): MarvelCharacter {

    return MarvelCharacter(
        id = 0,
        name = "",
        description = "",
        thumbnail = "",
        thumbnailExt = "",
        comics = listOf()
    )
}