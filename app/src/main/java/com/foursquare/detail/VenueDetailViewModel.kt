package com.foursquare.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foursquare.lifecycle.*
import com.foursquare.venue.data.VenueRepository
import kotlinx.coroutines.launch

class VenueDetailViewModel @ViewModelInject constructor(private val venueRepository: VenueRepository) :
    ViewModel() {

    private val mutableState = MutableLiveData<LiveDataEvent>()

    val state: LiveData<LiveDataEvent> = mutableState.nonNull()

    fun loadDetails(venueId: String) {
        mutableState.value = LoadingEvent
        viewModelScope.launch {
            try {
                mutableState.value = ContentEvent(venueRepository.detail(venueId))
            } catch (e: Throwable) {
                mutableState.value = ErrorEvent(e)
            }
        }
    }
}