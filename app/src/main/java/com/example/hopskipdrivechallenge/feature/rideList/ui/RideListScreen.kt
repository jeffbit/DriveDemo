package com.example.hopskipdrivechallenge.feature.rideList.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopskipdrivechallenge.R
import com.example.hopskipdrivechallenge.feature.rideList.model.RideUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.RidesByDateUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.RidesUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.toRideDateUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.toRidesUiModel
import com.example.hopskipdrivechallenge.feature.rideList.viewmodel.RideListViewModel
import com.example.hopskipdrivechallenge.feature.ridedetail.model.RideDetaiUiModel
import com.example.hopskipdrivechallenge.model.sampleRides


@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MyRidesScreen(
    rideListViewModel: RideListViewModel,
    onRideClick: (RidesByDateUiModel) -> Unit,
) {

    val viewState by rideListViewModel.rideListViewState.observeAsState()
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.my_rides)) },
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(onClick = {/*TODO*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu Button"
                        )
                    }
                }
            )
        }
    ) { padding ->

        MyRidesScreen(
            viewState = viewState!!,
            contentPadding = padding,
            onRideClick = onRideClick
        )
    }
}


@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
private fun MyRidesScreen(
    viewState: RideListViewModel.RideListViewState,
    contentPadding: PaddingValues,
    onRideClick: (RidesByDateUiModel) -> Unit,
    ) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = contentPadding.calculateStartPadding(
                LocalLayoutDirection.current
            ),
            end = contentPadding.calculateEndPadding(
                LocalLayoutDirection.current
            ),
            bottom = contentPadding.calculateBottomPadding() + 16.dp
        ),
    ) {
        when (viewState) {
            RideListViewModel.RideListViewState.Empty -> {
                item {

                }
            }
            RideListViewModel.RideListViewState.Error -> {
                item {
                    Text(text = "Error please try again")
                }

            }
            RideListViewModel.RideListViewState.Loading -> {
                item {
                    Row(
                        modifier = Modifier.fillParentMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

            }
            is RideListViewModel.RideListViewState.Success -> {
                myRidesList(
                    ridesUiModel = viewState.rides,
                    onRideClick = onRideClick,
                    lazyListScope = this
                )
            }
        }
    }

}


@ExperimentalMaterialApi
@ExperimentalFoundationApi
private fun myRidesList(
    ridesUiModel: RidesUiModel,
    onRideClick: (RidesByDateUiModel) -> Unit,
    lazyListScope: LazyListScope
) {

    with(lazyListScope) {
        ridesUiModel.rides.forEach { ride ->
            stickyHeader {
                RideListHeader(
                    startsAt = ride.startsAt,
                    endsAt = ride.endsAt,
                    dateOfRide = ride.date,
                    estimatedTotalCost = ride.estimated_earnings_cents
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            items(ride.rides) { rideModel ->
                RideCard(rideUiModel = rideModel,
                    onRideClick = { onRideClick(ride)})
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

@Composable
private fun RideListHeader(
    startsAt: String,
    endsAt: String,
    dateOfRide: String,
    estimatedTotalCost: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = dateOfRide, style = h1)
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontStyle = body1.fontStyle,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(startsAt)
                        }
                        append(" - ")
                        withStyle(style = SpanStyle(fontStyle = body1.fontStyle)) {
                            append(endsAt)
                        }
                    }
                )
            }
            Column(
                modifier = Modifier.padding(end = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.estimate),
                    style = caption
                )
                Text(
                    text = "$${estimatedTotalCost}",
                    style = body2
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun RideCard(
    rideUiModel: RideUiModel,
    onRideClick: (RideUiModel) -> Unit
) {
    Card(
        elevation = 2.dp,
        modifier = Modifier.padding(8.dp),
        onClick = { onRideClick( rideUiModel) }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row() {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontStyle = h2.fontStyle,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(rideUiModel.starts_at)
                        }
                        append(" - ")
                        withStyle(style = SpanStyle(fontStyle = h2.fontStyle)) {
                            append(rideUiModel.ends_at)
                            append(stringResource(R.string.space))
                        }
                        var passengers = 0;
                        var boosters = 0;

                        rideUiModel.ordered_waypoints.forEach {
                            passengers += it.passengers
                            boosters += it.booster_seats
                        }
                        withStyle(style = SpanStyle(fontStyle = body3.fontStyle)) {
                            if (passengers > 1) {
                                append(" (${passengers} riders")
                            } else append(" (${passengers} rider")

                            when (boosters) {
                                0 -> append(")")
                                1 -> append(" • ${boosters} booster)")
                                else -> append(" • ${boosters} boosters)")
                            }
                        }

                    },
                    modifier = Modifier.weight(1f)
                )
                Text(buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontStyle = body3.fontStyle
                        )
                    ) {
                        append(stringResource(R.string.estimate_short))
                    }
                    withStyle(style = SpanStyle(fontStyle = h2.fontStyle, color = Color.Blue)) {
                        append("$${rideUiModel.estimated_earnings_cents}")
                    }
                })
            }
            rideUiModel.ordered_waypoints.forEachIndexed { index, orderedWaypointUiModel ->
                Text(text = "${index + 1}. ${orderedWaypointUiModel.location.address}")
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}


@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview(device = Devices.PIXEL_3A_XL)
@Composable
fun PreviewRideCard() {
    MaterialTheme {
        Column() {
            LazyColumn() {
                myRidesList(
                    ridesUiModel = sampleRides.toRidesUiModel().toRideDateUiModel(),
                    onRideClick = {}, lazyListScope = this
                )
            }


        }
    }
}





