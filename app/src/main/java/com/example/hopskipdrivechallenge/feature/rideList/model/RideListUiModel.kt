package com.example.hopskipdrivechallenge.feature.rideList.model

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.hopskipdrivechallenge.model.Location
import com.example.hopskipdrivechallenge.model.OrderedWaypoint
import com.example.hopskipdrivechallenge.model.Passenger
import com.example.hopskipdrivechallenge.model.Ride
import com.example.hopskipdrivechallenge.model.Rides
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale


data class RidesUiModel(
    val rides: List<RidesByDateUiModel>
)

data class RidesByDateUiModel(
    val date: String,
    val startsAt: String,
    val endsAt: String,
    val estimated_earnings_cents: String,
    val rides: List<RideUiModel>
)

data class RideUiModel(
    val dateOfRide: String,
    val starts_at: String,
    val ends_at: String,
    val estimated_earnings_cents: String,
    val ordered_waypoints: List<OrderedWaypointUiModel>,
    val trip_id: String,
    val inSeries: Boolean,
    val estimatedRideMiles: Double,
    val estimatedRideMin: Int
)


fun toRidesUiModel(ridesUiModel: List<RideUiModel>): RidesUiModel {
    val rides = mutableListOf<RidesByDateUiModel>()
    val groupedByDate = ridesUiModel.groupBy { it.dateOfRide }

    groupedByDate.forEach { (date, ride) ->
        rides.add(
            RidesByDateUiModel(
                date = date,
                startsAt = ride.first().starts_at,
                endsAt = ride.last().ends_at,
                estimated_earnings_cents = addEstimatedEarnings(ride),
                rides = ride
            )
        )
    }
    return RidesUiModel(rides = rides)
}

fun List<RideUiModel>.toRideDateUiModel(): RidesUiModel {
    return toRidesUiModel(this)
}

fun Rides.toRidesUiModel(): List<RideUiModel> {
    val rides = mutableListOf<RideUiModel>()
    this.rides.forEach {
        rides.add(it.toRideUiModel())
    }
    return rides
}


private fun addEstimatedEarnings(rides: List<RideUiModel>): String {
    var total = 0.0
    rides.forEach {
        total += it.estimated_earnings_cents.toDouble()
    }
    return DecimalFormat("#.00").format(total)
}


data class OrderedWaypointUiModel(
    val id: String,
    val location: LocationUiModel,
    val passengers: Int,
    val booster_seats: Int,
    val anchor: Boolean,
    val lat: Double,
    val lon: Double
)

data class LocationUiModel(
    val address: String
)

private fun Ride.toRideUiModel() = RideUiModel(
    dateOfRide = dateTimeToLocalDate(starts_at),
    starts_at = dateTimeToLocalTime(starts_at),
    ends_at = dateTimeToLocalTime(ends_at),
    estimated_earnings_cents = formatEarnings(estimated_earnings_cents.toDouble()),
    ordered_waypoints = ordered_waypoints.map { it.toOrderedWayPointUiModel() },
    trip_id = trip_id.toString(),
    inSeries = in_series,
    estimatedRideMiles = estimated_ride_miles,
    estimatedRideMin = estimated_ride_minutes,

)

private fun formatEarnings(earnings: Double): String {
    return DecimalFormat("#.00").format(earnings * 1 / 100)
}

private fun OrderedWaypoint.toOrderedWayPointUiModel() = OrderedWaypointUiModel(
    id = id.toString(),
    location = locationEntity.toLocationUiModel(),
    passengers = passengers.size,
    booster_seats = countBoosterSeats(passengers = passengers),
    anchor = anchor,
    lat = locationEntity.lat,
    lon = locationEntity.lng,

    )


private fun countBoosterSeats(passengers: List<Passenger>): Int {
    var boosterCount = 0
    passengers.forEach {
        if (it.booster_seat) boosterCount++
    }
    return boosterCount
}

private fun Location.toLocationUiModel() = LocationUiModel(
    address = address
)


@SuppressLint("SimpleDateFormat")
private fun dateTimeToLocalTime(dateTime: String): String {
    val instant = Instant.parse(dateTime)
    val result = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime()
    val sdf = SimpleDateFormat("H:mm")
    val dateObj = sdf.parse("${result.hour}:${result.minute}")
    return SimpleDateFormat("K:mm a").format(dateObj)

}

@RequiresApi(Build.VERSION_CODES.O)
private fun dateTimeToLocalDate(dateTime: String): String {
    val instant = Instant.parse(dateTime)
    val result = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val date = result.toLocalDate()
    return "${
        date.dayOfWeek.getDisplayName(
            TextStyle.SHORT,
            Locale.ENGLISH
        )
    } ${date.month.value}/${date.dayOfMonth}"
}






