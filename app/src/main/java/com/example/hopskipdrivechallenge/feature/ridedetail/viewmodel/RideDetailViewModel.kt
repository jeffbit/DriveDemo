package com.example.hopskipdrivechallenge.feature.ridedetail.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hopskipdrivechallenge.data.repository.RidesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RideDetailViewModel @Inject constructor(private val ridesRepository: RidesRepository) :
    ViewModel() {


}