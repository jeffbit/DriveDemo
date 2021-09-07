package com.example.hopskipdrivechallenge.data.repository

import com.example.hopskipdrivechallenge.data.api.HopSkipDriveApi
import com.example.hopskipdrivechallenge.model.Rides
import com.example.hopskipdrivechallenge.model.toModel
import com.example.hopskipdrivechallenge.util.ResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class RidesRepositoryImpl(
    private val hopSkipDriveApi: HopSkipDriveApi,
    private val responseHandler: ResponseHandler
) : RidesRepository {

    override suspend fun returnSimplifiedRides(): Result<Rides> {
        return withContext(Dispatchers.IO) {
            responseHandler.process(hopSkipDriveApi.getSimplifiedRides())
                .mapCatching {
                    Timber.d("return simplified rides: ${it.toModel()}")
                    it.toModel()
                }
        }
    }

}