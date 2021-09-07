package com.example.hopskipdrivechallenge.data.repository

import com.example.hopskipdrivechallenge.model.Rides

interface RidesRepository {
    suspend fun returnSimplifiedRides():  Result<Rides>
}