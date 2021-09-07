package com.example.hopskipdrivechallenge.model

import com.example.hopskipdrivechallenge.data.model.LocationEntity
import com.example.hopskipdrivechallenge.data.model.OrderedWaypointEntity
import com.example.hopskipdrivechallenge.data.model.PassengerEntity
import com.example.hopskipdrivechallenge.data.model.RideEntity
import com.example.hopskipdrivechallenge.data.model.RidesEntity


data class Rides(
    val rides: List<Ride>
)

data class Ride(
    val ends_at: String,
    val estimated_earnings_cents: Int,
    val estimated_ride_miles: Double,
    val estimated_ride_minutes: Int,
    val in_series: Boolean,
    val ordered_waypoints: List<OrderedWaypoint>,
    val starts_at: String,
    val trip_id: Int
)

data class Passenger(
    val booster_seat: Boolean,
    val first_name: String,
    val id: Int
)

data class OrderedWaypoint(
    val anchor: Boolean,
    val id: Int,
    val locationEntity: Location,
    val passengers: List<Passenger>,
)

data class Location(
    val address: String,
    val lat: Double,
    val lng: Double
)


fun RidesEntity.toModel() = Rides(
    rides = rides.map { it.toModel() }
)

fun RideEntity.toModel() = Ride(
    ends_at = endsAt,
    estimated_earnings_cents = estimatedEarningsCents,
    estimated_ride_miles = estimatedRideMiles,
    estimated_ride_minutes = estimatedRideMinutes,
    in_series = inSeries,
    ordered_waypoints = orderedWaypoints.toOrderedWayPointList(),
    starts_at = startsAt,
    trip_id = tripId
)

fun List<OrderedWaypointEntity>.toOrderedWayPointList(): List<OrderedWaypoint> {
    return this.map { it.toModel() } ?: emptyList()

}

fun PassengerEntity.toModel() = Passenger(
    booster_seat = boosterSeat,
    first_name = firstName,
    id = id
)

fun OrderedWaypointEntity.toModel() = OrderedWaypoint(
    anchor = anchor,
    id = id,
    locationEntity = location.toModel(),
    passengers = passengers.map { it.toModel() }

)

fun LocationEntity.toModel() = Location(
    address = address,
    lat = lat,
    lng = lng
)


//sample data for composable


val sampleLocation = Location(
    address = "2565 E Underhill Ave, Anaheim 92806",
    lat = 34.17006916353578,
    lng = -118.43274040504944
)
val samplePassenger = Passenger(
    booster_seat = true,
    first_name = "John",
    id = 121
)

val sampleOrderedWaypoint = OrderedWaypoint(
    anchor = true,
    id = 222,
    locationEntity = sampleLocation,
    passengers = listOf(
        samplePassenger,
        samplePassenger.copy(booster_seat = false)
    )
)
val sampleRiderData = Ride(
    ends_at = "2021-06-17T14:37:00Z",
    estimated_earnings_cents = 6071,
    estimated_ride_miles = 18.48,
    estimated_ride_minutes = 50,
    in_series = false,
    ordered_waypoints = listOf(sampleOrderedWaypoint),
    starts_at = "2021-06-17T11:18:00Z",
    trip_id = 123456
)

val sampleRides = Rides(
    rides = listOf(
        sampleRiderData,
        sampleRiderData,
        sampleRiderData, sampleRiderData
    )
)



