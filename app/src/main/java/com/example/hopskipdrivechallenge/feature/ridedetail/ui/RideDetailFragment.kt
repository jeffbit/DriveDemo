package com.example.hopskipdrivechallenge.feature.ridedetail.ui

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hopskipdrivechallenge.R
import com.example.hopskipdrivechallenge.databinding.FragmentRideDetailBinding
import com.example.hopskipdrivechallenge.feature.ridedetail.model.RideDetailUiModel
import com.example.hopskipdrivechallenge.shared.viewmodel.RideListDetailViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RideDetailFragment : Fragment() {

    private val detailViewModel: RideListDetailViewModel by activityViewModels<RideListDetailViewModel>()


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

        locationAdapter = LocationAdapter(emptyList())
        binding.recyclerView.adapter = locationAdapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.adapter = locationAdapter
        navigateBack(binding.toolbar)
        loadData()


    }


    private fun loadData() {
        detailViewModel.rideDetailViewState.observe(viewLifecycleOwner, { viewState ->
            when (viewState) {
                RideListDetailViewModel.RideDetailViewState.Empty -> {
                    //todo
                }
                RideListDetailViewModel.RideDetailViewState.Error -> {
                    //todo
                }
                RideListDetailViewModel.RideDetailViewState.Loading -> {
                    //todo

                }
                is RideListDetailViewModel.RideDetailViewState.Success -> {
                    successStateUi(viewState.ride)
                }
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun successStateUi(rides: RideDetailUiModel) {
        when (rides.inSeries) {
            true -> {
                binding.seriesTV.isVisible = true
                binding.seriesTV.isInvisible = false
            }
            false -> {
                binding.seriesTV.isInvisible = true
                binding.seriesTV.isVisible = false

            }
        }
        binding.date.text = "${rides.date} • ${rides.startsAt} - ${rides.endsAt}"
        binding.tripIdTv.text =
            "Trip ID: ${rides.tripId} • ${rides.totalMiles} mi • ${rides.totalTimeInMin} min"
        binding.estimatedCost.text = "$${rides.estimatedCost}"
        locationAdapter.updateList(rides.listOfLocations)
        val mapFrag = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFrag.getMapAsync { googleMap ->
            rides.listOfLocations.forEach { location ->
                googleMap.addMarker(MarkerOptions().position(LatLng(location.lat, location.lon)))
            }
            googleMap.setOnMapLoadedCallback {
                val bounds = LatLngBounds.builder()
                rides.listOfLocations.forEach { bounds.include((LatLng(it.lat, it.lon))) }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 150))
            }
        }
    }



    private fun navigateBack(toolbar: androidx.appcompat.widget.Toolbar) {
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }


}