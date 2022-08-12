package com.karlosprojects.characters_data.di

import com.karlosprojects.characters_data.repository.CharacterDetailRepositoryImpl
import com.karlosprojects.characters_data.repository.CharactersRepositoryImpl
import com.karlosprojects.characters_domain.repository.CharacterDetailRepository
import com.karlosprojects.characters_domain.repository.CharactersRepository
import com.karlosprojects.core_network.api.MarvelApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CharactersDataModule {

    @Provides
    @Singleton
    fun provideCharactersRepository(
        api: MarvelApi
    ): CharactersRepository {
        return CharactersRepositoryImpl(marvelApi = api)
    }

    @Provides
    @Singleton
    fun provideCharacterDetailRepository(
        api: MarvelApi
    ): CharacterDetailRepository {
        return CharacterDetailRepositoryImpl(marvelApi = api)
    }
}