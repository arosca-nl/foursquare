package com.foursquare.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.foursquare.venue.data.VenueDetail
import com.foursquare.venue.data.VenueRepository
import kotlinx.coroutines.launch

class VenueDetailViewModel @ViewModelInject constructor(private val venueRepository: VenueRepository) :
    ViewModel() {

    private val mutableDetail = MutableLiveData<Result<VenueDetail>>()

    val detail: LiveData<Result<VenueDetail>> = MediatorLiveData<Result<VenueDetail>>().apply {
        addSource(mutableDetail) { if (it != null) value = it }
    }

    fun loadDetails(venueId: String) {
        viewModelScope.launch {
            mutableDetail.value = runCatching { venueRepository.detail(venueId) }
        }
    }
}