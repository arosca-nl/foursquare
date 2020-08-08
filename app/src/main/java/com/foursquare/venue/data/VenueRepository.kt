package com.foursquare.venue.data

import com.foursquare.venue.api.VenueApi
import com.foursquare.venue.dao.VenueDao
import com.foursquare.venue.dao.toData
import com.foursquare.venue.dao.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VenueRepository @Inject constructor(
    private val venueApi: VenueApi,
    private val venueDao: VenueDao
) {

    suspend fun search(near: String) = withContext(Dispatchers.IO) {
        // get fresh data
        val result = runCatching { venueApi.search(near).venues }
        if (result.isSuccess) {
            result.getOrThrow().apply {
                // cache data
                venueDao.insertVenues(this.toEntity())
            }
        } else {
            // check cache, return data or initial error
            val venues = venueDao.search(near)
            if (venues.isEmpty()) result.getOrThrow() else venues.toData()
        }
    }

    suspend fun detail(venueId: String) = withContext(Dispatchers.IO) {
        // get fresh data
        val result = runCatching { venueApi.detail(venueId).detail }
        if (result.isSuccess) {
            result.getOrThrow().apply {
                // cache data
                venueDao.insertVenueDetail(this.toEntity())
            }
        } else {
            // check cache, return data or initial error
            val venueDetailEntity = venueDao.searchVenueDetail(venueId)
            venueDetailEntity?.toData() ?: result.getOrThrow()
        }
    }
}