package com.example.hopskipdrivechallenge.feature.ridedetail.ui

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
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

        locationAdapter = LocationAdapter(emptyList(), view.context)
        binding.recyclerView.adapter = locationAdapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.adapter = locationAdapter
        navigateBack(binding.toolbar)
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun loadData() {
        detailViewModel.rideDetailViewState.observe(viewLifecycleOwner, { viewState ->
            when (viewState) {
                RideListDetailViewModel.RideDetailViewState.Empty -> {
                    //todo : empty State
                }
                RideListDetailViewModel.RideDetailViewState.Error -> {
                    //todo : Error State
                }
                RideListDetailViewModel.RideDetailViewState.Loading -> {
                    //todo Loading State

                }
                is RideListDetailViewModel.RideDetailViewState.Success -> {
                    successStateUi(viewState.ride)
                }
            }
        })
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
        val dateSpan = SpannableString("${rides.date} • ${rides.startsAt} - ${rides.endsAt}")
        dateSpan.setSpan(ForegroundColorSpan(Color.Blue.hashCode()), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        dateSpan.setSpan(StyleSpan(Typeface.BOLD), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        dateSpan.setSpan(RelativeSizeSpan(2f), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        dateSpan.setSpan(StyleSpan(Typeface.BOLD), 10, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.date.text = dateSpan


        val tripSpan = "Trip ID: ${rides.tripId} • ${rides.totalMiles} mi • ${rides.totalTimeInMin} min"
        binding.estimatedCost.text = "$${rides.estimatedCost}"
        binding.tripIdTv.text = tripSpan

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