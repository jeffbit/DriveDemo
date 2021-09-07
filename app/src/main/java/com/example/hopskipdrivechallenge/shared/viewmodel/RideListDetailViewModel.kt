package com.example.hopskipdrivechallenge.shared.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopskipdrivechallenge.data.repository.RidesRepository
import com.example.hopskipdrivechallenge.feature.rideList.model.RideUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.RidesUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.toRideDateUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.toRidesUiModel
import com.example.hopskipdrivechallenge.feature.ridedetail.model.RideDetailUiModel
import com.example.hopskipdrivechallenge.feature.ridedetail.model.toRideDetailUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class RideListDetailViewModel @Inject constructor(
    private val ridesRepository: RidesRepository
) : ViewModel() {


    private val mutableRideListViewState: MutableLiveData<RideListViewState> =
        MutableLiveData(RideListViewState.Empty)
    val rideListViewState: LiveData<RideListViewState> = mutableRideListViewState


    private val mutableRideDetailViewState: MutableLiveData<RideDetailViewState> =
        MutableLiveData(RideDetailViewState.Empty)
    val rideDetailViewState: LiveData<RideDetailViewState> = mutableRideDetailViewState


    fun setRideDetail(ride: RideUiModel) {
        mutableRideDetailViewState.postValue(RideDetailViewState.Loading)
        mutableRideDetailViewState.postValue(RideDetailViewState.Success(ride = ride.toRideDetailUiModel()))
    }

    fun retrieveMyRides() {
        mutableRideListViewState.postValue(RideListViewState.Loading)
        viewModelScope.launch {
            ridesRepository.returnSimplifiedRides()
                .onSuccess {
                    mutableRideListViewState.postValue(
                        RideListViewState.Success(
                            it.toRidesUiModel().toRideDateUiModel()
                        )
                    )
                    Timber.d("Rides: ${it.toRidesUiModel()}")
                }
                .onFailure {
                    mutableRideListViewState.postValue(RideListViewState.Error)
                    Timber.d("Failure ${it.localizedMessage}")
                }

        }
    }

    //RideDetailFragment ViewState
    sealed class RideDetailViewState {
        object Empty : RideDetailViewState()
        object Error : RideDetailViewState()
        object Loading : RideDetailViewState()
        data class Success(
            val ride: RideDetailUiModel
        ) : RideDetailViewState()
    }

    //RideListScreen ViewState
    sealed class RideListViewState {
        object Empty : RideListViewState()
        object Error : RideListViewState()
        object Loading : RideListViewState()
        data class Success(
            val rides: RidesUiModel
        ) : RideListViewState()
    }
}