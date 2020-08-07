package com.foursquare.venue.data

import com.foursquare.venue.api.VenueApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VenueRepository @Inject constructor(private val venueApi: VenueApi) {

    suspend fun search(near: String) = withContext(Dispatchers.IO) {
        venueApi.search(near).venues
    }
}