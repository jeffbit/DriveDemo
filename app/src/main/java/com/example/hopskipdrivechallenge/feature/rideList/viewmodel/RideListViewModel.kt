package com.example.hopskipdrivechallenge.feature.rideList.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopskipdrivechallenge.data.repository.RidesRepository
import com.example.hopskipdrivechallenge.feature.rideList.model.RidesUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.toRideDateUiModel
import com.example.hopskipdrivechallenge.feature.rideList.model.toRidesUiModel
import com.example.hopskipdrivechallenge.model.Ride
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class RideListViewModel @Inject constructor(
    private val ridesRepository: RidesRepository
) : ViewModel() {


    private val mutableViewState: MutableLiveData<RideListViewState> =
        MutableLiveData(RideListViewState.Empty)
    val viewState: LiveData<RideListViewState> = mutableViewState


    fun retrieveMyRides() {
        mutableViewState.postValue(RideListViewState.Loading)
        viewModelScope.launch {
            ridesRepository.returnSimplifiedRides()
                .onSuccess {
                    mutableViewState.postValue(
                        RideListViewState.Success(
                            it.toRidesUiModel().toRideDateUiModel()
                        )
                    )
                    Timber.d("Rides: ${it.toRidesUiModel()}")
                }
                .onFailure {
                    mutableViewState.postValue(RideListViewState.Error)
                    Timber.d("Failure ${it.localizedMessage}")
                }

        }
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