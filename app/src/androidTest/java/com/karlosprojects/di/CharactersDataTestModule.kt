package com.karlosprojects.di

import com.karlosprojects.characters_data.di.CharactersDataModule
import com.karlosprojects.characters_data.remote.MarvelApi
import com.karlosprojects.characters_data.repository.CharacterDetailRepositoryImpl
import com.karlosprojects.characters_data.repository.CharactersRepositoryImpl
import com.karlosprojects.characters_domain.repository.CharacterDetailRepository
import com.karlosprojects.characters_domain.repository.CharactersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CharactersDataModule::class]
)
object CharactersDataTestModule {

    @Provides
    @Singleton
    fun provideTestMarvelApi(client: OkHttpClient): MarvelApi {
        return Retrofit.Builder()
            .baseUrl("http://localhost:8080/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideTestOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

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