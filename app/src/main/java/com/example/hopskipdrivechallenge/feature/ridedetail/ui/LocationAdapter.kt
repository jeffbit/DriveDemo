package com.example.hopskipdrivechallenge.feature.ridedetail.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hopskipdrivechallenge.R
import com.example.hopskipdrivechallenge.feature.rideList.model.OrderedWaypointUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.RidesByDateUiModel
import com.example.hopskipdrivechallenge.feature.ridedetail.model.RideDetailLocation

class LocationAdapter(private var list: List<RideDetailLocation>) :
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    fun updateList(newList: List<RideDetailLocation>) {
        val diffCallBack = LocationDiffCallBack(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        this.list = emptyList()
        this.list = newList
        diffResult.dispatchUpdatesTo(this)
    }


    inner class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var iconImage = view.findViewById<ImageView>(R.id.iconIv)
        private var pickUpOrDropOff = view.findViewById<TextView>(R.id.pickupTv)
        private var location = view.findViewById<TextView>(R.id.locationTv)


        fun bind(rideDetailLocation: RideDetailLocation) {
            location.text = rideDetailLocation.address
            when (rideDetailLocation.anchor) {
                true -> {
                    iconImage.setImageResource(R.drawable.ic_baseline_star_24)
                }
                false -> {
                    iconImage.setImageResource(R.drawable.ic_baseline_check_circle_24)
                }
            }
            if (rideDetailLocation == list.last()) {
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


class LocationDiffCallBack(
    private var oldList: List<RideDetailLocation>,
    private var newList: List<RideDetailLocation>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].lat == newList[newItemPosition].lat
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}