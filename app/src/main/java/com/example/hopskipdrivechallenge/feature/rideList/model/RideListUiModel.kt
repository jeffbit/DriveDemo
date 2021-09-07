package com.example.hopskipdrivechallenge.feature.rideList.model

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.hopskipdrivechallenge.model.Location
import com.example.hopskipdrivechallenge.model.OrderedWaypoint
import com.example.hopskipdrivechallenge.model.Passenger
import com.example.hopskipdrivechallenge.model.Ride
import com.example.hopskipdrivechallenge.model.Rides
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
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
    val estimated_earnings_cents: Double,
    val ordered_waypoints: List<OrderedWaypointUiModel>,
    val trip_id: String
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

fun List<RideUiModel>.toRideDateUiModel(): RidesUiModel{
    return toRidesUiModel(this)

}

fun Rides.toRidesUiModel(): List<RideUiModel> {
    val rides = mutableListOf<RideUiModel>()
    this.rides.forEach {
        rides.add(it.toRideUiModel())
    }
    return rides
}


fun addEstimatedEarnings(rides: List<RideUiModel>): String {
    var total = 0.0
    rides.forEach {
        total += it.estimated_earnings_cents
    }
    return total.toString()
}


data class OrderedWaypointUiModel(
    val id: String,
    val location: LocationUiModel,
    val passengers: Int,
    val booster_seats: Int
)

data class LocationUiModel(
    val address: String
)

fun Ride.toRideUiModel() = RideUiModel(
    dateOfRide = dateTimeToLocalDate(starts_at),
    starts_at = dateTimeToLocalTime(starts_at),
    ends_at = dateTimeToLocalTime(ends_at),
    estimated_earnings_cents = (estimated_earnings_cents.toDouble() * 1 / (100)),
    ordered_waypoints = ordered_waypoints.map { it.toOrderedWayPointUiModel() },
    trip_id = trip_id.toString()
)

fun OrderedWaypoint.toOrderedWayPointUiModel() = OrderedWaypointUiModel(
    id = id.toString(),
    location = locationEntity.toLocationUiModel(),
    passengers = passengers.size,
    booster_seats = countBoosterSeats(passengers = passengers)
)


fun countBoosterSeats(passengers: List<Passenger>): Int {
    var boosterCount = 0
    passengers.forEach {
        if (it.booster_seat) boosterCount++

    }
    return boosterCount
}

fun Location.toLocationUiModel() = LocationUiModel(
    address = address
)


@SuppressLint("SimpleDateFormat")
fun dateTimeToLocalTime(dateTime: String): String {
    val instant = Instant.parse(dateTime)
    val result = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.id)).toLocalTime()
    val sdf = SimpleDateFormat("H:mm")
    val dateObj = sdf.parse("${result.hour}:${result.minute}")
    return SimpleDateFormat("K:mm a").format(dateObj)

}

@RequiresApi(Build.VERSION_CODES.O)
fun dateTimeToLocalDate(dateTime: String): String {
    val instant = Instant.parse(dateTime)
    val result = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.id))
    val date = result.toLocalDate()
    return "${
        date.dayOfWeek.getDisplayName(
            TextStyle.SHORT,
            Locale.ENGLISH
        )
    } ${date.month.value}/${date.dayOfMonth}"
}






