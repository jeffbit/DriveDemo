package com.example.hopskipdrivechallenge.feature.ridedetail.model

import com.example.hopskipdrivechallenge.feature.rideList.model.OrderedWaypointUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.RideUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.RidesByDateUiModel
import com.example.hopskipdrivechallenge.model.OrderedWaypoint
import java.text.DecimalFormat

data class RideDetailUiModel(
    val tripId: String,
    val date: String,
    val startsAt: String,
    val endsAt: String,
    val estimatedCost: String,
    val totalMiles: String,
    val totalTimeInMin: String,
    val inSeries: Boolean,
    val listOfLocations: List<RideDetailLocation>
)


data class RideDetailLocation(
    val anchor: Boolean,
    val address: String,
    val lat: Double,
    val lon: Double
)
fun RideUiModel.toRideDetailUiModel() = RideDetailUiModel(
    tripId = trip_id,
    date = dateOfRide,
    startsAt = starts_at,
    endsAt = ends_at,
    estimatedCost = estimated_earnings_cents,
    totalMiles = estimatedRideMiles.toString(),
    totalTimeInMin = estimatedRideMin.toString(),
    inSeries = inSeries,
    listOfLocations = convertRidesToLocation(wayPoints = ordered_waypoints)
)

fun convertRidesToLocation(wayPoints: List<OrderedWaypointUiModel>): List<RideDetailLocation> {
    val list = mutableListOf<RideDetailLocation>()
    wayPoints.forEach { waypoint ->
        list.add(
            RideDetailLocation(
                anchor = waypoint.anchor,
                address = waypoint.location.address,
                lat = waypoint.lat,
                lon = waypoint.lon
            )
        )
    }
    return list
}


