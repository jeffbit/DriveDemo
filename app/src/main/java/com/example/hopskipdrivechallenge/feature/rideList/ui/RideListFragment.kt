package com.example.hopskipdrivechallenge.feature.rideList.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.hopskipdrivechallenge.shared.viewmodel.RideListDetailViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RideListFragment : Fragment() {

    private val detailViewModel: RideListDetailViewModel by activityViewModels<RideListDetailViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailViewModel.retrieveMyRides()
    }

    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    MyRidesScreen(
                        rideListDetailViewModel = detailViewModel,
                        onRideClick = { ride ->
                            detailViewModel.setRideDetail(ride = ride)
                            passDataToRideDetail("")
                        })
                }
            }
        }
    }

    private fun passDataToRideDetail(tripId: String) {
        val action = RideListFragmentDirections.actionRideListFragmentToRideDetailFragment(tripId)
        findNavController().navigate(action)
    }

}