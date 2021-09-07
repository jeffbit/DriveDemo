package com.example.hopskipdrivechallenge.di

import com.example.hopskipdrivechallenge.data.api.HopSkipDriveApi
import com.example.hopskipdrivechallenge.data.repository.RidesRepository
import com.example.hopskipdrivechallenge.data.repository.RidesRepositoryImpl
import com.example.hopskipdrivechallenge.util.ResponseHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object RideRepositoryModule {

    @Provides
    fun provideRideRepository(
        hopSkipDriveApi: HopSkipDriveApi,
        responseHandler: ResponseHandler
    ): RidesRepository {
        return RidesRepositoryImpl(
            hopSkipDriveApi = hopSkipDriveApi,
            responseHandler = responseHandler
        )
    }
}