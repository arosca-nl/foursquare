package com.foursquare.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foursquare.lifecycle.*
import com.foursquare.venue.data.Venue
import com.foursquare.venue.data.VenueRepository
import kotlinx.coroutines.launch

class VenueSearchViewModel @ViewModelInject constructor(private val venueRepository: VenueRepository) :
    ViewModel() {

    private val mutableState = MutableLiveData<LiveDataEvent>()

    val state: LiveData<LiveDataEvent> = mutableState.nonNull()

    fun search(near: String?) {
        mutableState.value = LoadingEvent

        if (near.isNullOrEmpty())
            mutableState.value = ContentEvent(emptyList<Venue>())
        else viewModelScope.launch {
            try {
                mutableState.value = ContentEvent(venueRepository.search(near))
            } catch (e: Throwable) {
                mutableState.value = ErrorEvent(e)
            }
        }
    }
}