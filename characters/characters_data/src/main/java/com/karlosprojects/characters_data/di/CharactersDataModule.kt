package com.karlosprojects.characters_data.di

import com.karlosprojects.characters_data.remote.MarvelApi
import com.karlosprojects.characters_data.repository.CharactersRepositoryImpl
import com.karlosprojects.characters_domain.repository.CharactersRepository
import com.karlosprojects.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CharactersDataModule {

    @Provides
    @Singleton
    fun provideMarvelApi(client: OkHttpClient): MarvelApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
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
}