package com.example.hopskipdrivechallenge.di

import com.example.hopskipdrivechallenge.data.api.HopSkipDriveApi
import com.example.hopskipdrivechallenge.data.api.RetrofitClient
import com.example.hopskipdrivechallenge.util.ResponseHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): HopSkipDriveApi {
        return RetrofitClient.hopSkipDriveApi
    }


    @Provides
    fun provideResponseHandler(): ResponseHandler {
        return ResponseHandler()
    }
}