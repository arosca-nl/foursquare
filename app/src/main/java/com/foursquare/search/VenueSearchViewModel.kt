package com.foursquare.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.foursquare.venue.data.Venue
import com.foursquare.venue.data.VenueRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class VenueSearchViewModel @ViewModelInject constructor(private val venueRepository: VenueRepository) : ViewModel() {

    private val mutableVenues = MutableLiveData<VenuesResult>()
    private var searchJob: Job? = null

    val venues: LiveData<VenuesResult> = MediatorLiveData<VenuesResult>().apply {
        addSource(mutableVenues) { if (it != null) value = it }
    }

    fun search(near: String?) {
        searchJob?.cancel()

        if (near.isNullOrEmpty())
            mutableVenues.value = Result.success(emptyList())
        else searchJob = viewModelScope.launch {
            val result = runCatching { venueRepository.search(near) }
            // hide cancel
            if (result.exceptionOrNull() !is CancellationException)
                mutableVenues.value = result
        }
    }
}

typealias VenuesResult = Result<List<Venue>>