package com.example.hopskipdrivechallenge.feature.ridedetail.model

import com.example.hopskipdrivechallenge.feature.rideList.model.OrderedWaypointUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.RidesByDateUiModel
import com.example.hopskipdrivechallenge.model.OrderedWaypoint

data class RideDetailUiModel(
    val tripId: String,
    val date: String,
    val startsAt: String,
    val endsAt: String,
    val estimatedCost: String,
    val totalMiles: String,
    val totalTimeInMin: String,
    val listOfLocations: List<RideDetailLocation>
)


data class RideDetailLocation(
    val anchor: Boolean,
    val address: String,
    val lat: Double,
    val lon: Double
)



fun RidesByDateUiModel.toRideDetailUiModel() = RideDetailUiModel(
    tripId = this.rides[0].trip_id,
    date = date,
    startsAt = startsAt,
    endsAt = endsAt,
    estimatedCost = estimated_earnings_cents,
    totalMiles = "",
    totalTimeInMin = "",
    listOfLocations = this.rides.forEach { it.ordered_waypoints.forEach { it.location } }

)

fun convertWayPointTOLOCAITON


fun OrderedWaypoint.toRideDetailLocations() = RideDetailLocation(
    anchor = anchor,
    address = locationEntity.address,
    lat = locationEntity.lat,
    lon = locationEntity.lng
)
