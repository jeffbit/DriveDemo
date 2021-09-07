package com.example.hopskipdrivechallenge.feature.ridedetail.ui

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hopskipdrivechallenge.MainActivity
import com.example.hopskipdrivechallenge.R
import com.example.hopskipdrivechallenge.databinding.FragmentRideDetailBinding
import com.example.hopskipdrivechallenge.feature.rideList.viewmodel.RideListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RideDetailFragment : Fragment() {

    private val viewModel: RideListViewModel by activityViewModels<RideListViewModel>()


    private lateinit var locationAdapter: LocationAdapter
    private var _binding: FragmentRideDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRideDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        navigateBack(binding.toolbar)

        initAdapter(view = view)
        loadData()


    }


    private fun loadData() {
        viewModel.rideDetailViewState.observe(viewLifecycleOwner, { viewState ->
            when (viewState) {
                RideListViewModel.RideDetailViewState.Empty -> {
                    //todo
                }
                RideListViewModel.RideDetailViewState.Error -> {
                    //todo
                }
                RideListViewModel.RideDetailViewState.Loading -> {
                    //todo

                }
                is RideListViewModel.RideDetailViewState.Success -> {
                    val rides = viewState.ride
                    binding.date.text = rides.date + " " + rides.startsAt +" - " + rides.endsAt
                    binding.tripIdTv.text = rides.rides[0].trip_id
                    binding.estimatedCost.text = "$${rides.estimated_earnings_cents}"
                    locationAdapter = LocationAdapter(viewState.ride.rides[0].ordered_waypoints)


                }
            }

        })

    }

    private fun initAdapter(view: View) {
        locationAdapter = LocationAdapter(emptyList())
        binding.recyclerView.apply {
            adapter = locationAdapter
            setHasFixedSize(true)
            LinearLayoutManager(view.context)
        }
    }

    private fun navigateBack(toolbar: androidx.appcompat.widget.Toolbar) {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}