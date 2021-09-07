package com.example.hopskipdrivechallenge.data.model

import com.google.gson.annotations.SerializedName

data class RidesEntity(
    @SerializedName("rides")
    val rides: List<RideEntity>
)

data class RideEntity(
    @SerializedName("ends_at")
    val endsAt: String,
    @SerializedName("estimated_earnings_cents")
    val estimatedEarningsCents: Int,
    @SerializedName("estimated_ride_miles")
    val estimatedRideMiles: Double,
    @SerializedName("estimated_ride_minutes")
    val estimatedRideMinutes: Int,
    @SerializedName("in_series")
    val inSeries: Boolean,
    @SerializedName("ordered_waypoints")
    val orderedWaypoints: List<OrderedWaypointEntity>,
    @SerializedName("starts_at")
    val startsAt: String,
    @SerializedName("trip_id")
    val tripId: Int
)

data class PassengerEntity(
    @SerializedName("booster_seat")
    val boosterSeat: Boolean,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("id")
    val id: Int
)

data class OrderedWaypointEntity(
    @SerializedName("anchor")
    val anchor: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("location")
    val location: LocationEntity,
    @SerializedName("passengers")
    val passengers: List<PassengerEntity>
)

data class LocationEntity(
    @SerializedName("address")
    val address: String,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)


