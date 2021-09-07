package com.example.hopskipdrivechallenge.feature.rideList.ui

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.hopskipdrivechallenge.feature.rideList.viewmodel.RideListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RideListFragment : Fragment() {

    private val viewModel: RideListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            viewModel.retrieveMyRides()
        }, 5000)


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
                        rideListViewModel = viewModel,
                        onRideClick = { tripId ->
                            passDataToRideDetail(tripId = tripId)
                        })
                }
            }
        }
    }

    private fun passDataToRideDetail(tripId: String) {
//        val action = RideListFragmentDirections.actionRideListFragmentToRideDetailFragment(tripId)
//        findNavController().navigate(action)
    }

}