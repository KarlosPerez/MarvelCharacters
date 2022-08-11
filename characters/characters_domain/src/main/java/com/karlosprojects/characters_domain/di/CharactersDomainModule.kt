package com.karlosprojects.characters_domain.di

import com.karlosprojects.characters_domain.repository.CharacterDetailRepository
import com.karlosprojects.characters_domain.repository.CharactersRepository
import com.karlosprojects.characters_domain.usecases.GetCharacterDetail
import com.karlosprojects.characters_domain.usecases.GetCharacters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object CharactersDomainModule {

    @ViewModelScoped
    @Provides
    fun provideRequestCharactersUC(
        repository: CharactersRepository
    ): GetCharacters {
        return GetCharacters(repository)
    }

    @ViewModelScoped
    @Provides
    fun provideRequestCharacterDetailUC(
        repository: CharacterDetailRepository
    ): GetCharacterDetail {
        return GetCharacterDetail(repository)
    }
}