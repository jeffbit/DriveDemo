package com.example.hopskipdrivechallenge.data.api

import com.example.hopskipdrivechallenge.data.model.RidesEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface HopSkipDriveApi {

    @GET
    suspend fun getSimplifiedRides(
        @Url  url : String = "https://storage.googleapis.com/hsd-interview-resources/simplified_my_rides_response.json"): Response<RidesEntity>
}