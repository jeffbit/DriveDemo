package com.example.hopskipdrivechallenge.feature.rideList.viewmodel

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopskipdrivechallenge.data.repository.RidesRepository
import com.example.hopskipdrivechallenge.feature.rideList.model.RidesByDateUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.RidesUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.toRideDateUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.toRidesUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class RideListViewModel @Inject constructor(
    private val ridesRepository: RidesRepository
) : ViewModel() {


    private val mutableRideListViewState: MutableLiveData<RideListViewState> =
        MutableLiveData(RideListViewState.Empty)
    val rideListViewState: LiveData<RideListViewState> = mutableRideListViewState


    private val mutableRideDetailViewState : MutableLiveData<RideDetailViewState> =
        MutableLiveData(RideDetailViewState.Empty)
    val rideDetailViewState : LiveData<RideDetailViewState> = mutableRideDetailViewState


     fun setRideDetail(ride: RidesByDateUiModel){
        mutableRideDetailViewState.postValue(RideDetailViewState.Loading)
            mutableRideDetailViewState.postValue(RideDetailViewState.Success(ride = ride))
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


    sealed class RideDetailViewState {
        object Empty : RideDetailViewState()
        object Error : RideDetailViewState()
        object Loading : RideDetailViewState()
        data class Success(
            val ride: RidesByDateUiModel
        ) : RideDetailViewState()

    }


    sealed class RideListViewState {
        object Empty : RideListViewState()
        object Error : RideListViewState()
        object Loading : RideListViewState()
        data class Success(
            val rides: RidesUiModel
        ) : RideListViewState()

    }
}