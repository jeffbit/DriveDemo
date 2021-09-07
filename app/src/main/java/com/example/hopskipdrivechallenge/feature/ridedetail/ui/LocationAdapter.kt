package com.example.hopskipdrivechallenge.feature.ridedetail.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hopskipdrivechallenge.R
import com.example.hopskipdrivechallenge.feature.rideList.model.OrderedWaypointUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.RidesByDateUiModel

class LocationAdapter(private val list: List<OrderedWaypointUiModel>) :
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {


    inner class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var iconImage = view.findViewById<ImageView>(R.id.iconIv)
        private var pickUpOrDropOff = view.findViewById<TextView>(R.id.pickupTv)
        private var location = view.findViewById<TextView>(R.id.locationTv)


        fun bind(orderedWaypoint: OrderedWaypointUiModel) {
            location.text = orderedWaypoint.location.address
            when (orderedWaypoint.anchor) {
                true -> {
                    iconImage.setImageResource(R.drawable.ic_baseline_star_24)
                }
                false -> {
                    iconImage.setImageResource(R.drawable.ic_baseline_check_circle_24)
                }
            }
            if (orderedWaypoint == list.last()) {
                pickUpOrDropOff.text = "Drop-off"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.pickup_points, parent, false)

        return LocationViewHolder(view = view)
    }

    override fun onBindViewHolder(holderLocation: LocationViewHolder, position: Int) {
        val currentWayPoint = list[position]
        holderLocation.bind(currentWayPoint)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}