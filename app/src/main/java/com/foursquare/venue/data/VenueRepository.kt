package com.foursquare.venue.data

import com.foursquare.venue.api.VenueApi
import com.foursquare.venue.dao.CacheDatabase
import com.foursquare.venue.dao.toData
import com.foursquare.venue.dao.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VenueRepository @Inject constructor(
    private val venueApi: VenueApi,
    private val cacheDatabase: CacheDatabase
) {

    suspend fun search(near: String) = withContext(Dispatchers.IO) {
        // get fresh data
        val result = runCatching { venueApi.search(near).venues }
        return@withContext if (result.isSuccess) {
            result.getOrThrow().apply {
                // cache data
                cacheDatabase.venuesDao.insertVenues(this.toEntity())
            }
        } else {
            // check cache,return data or initial error
            val venues = cacheDatabase.venuesDao.search(near)
            if (venues.isEmpty()) result.getOrThrow() else venues.toData()
        }
    }

    suspend fun detail(venueId: String) = withContext(Dispatchers.IO) {
        // get fresh data
        val result = runCatching { venueApi.detail(venueId).detail }
        return@withContext if (result.isSuccess) {
            result.getOrThrow().apply {
                // cache data
                cacheDatabase.venuesDao.insertVenueDetail(this.toEntity())
            }
        } else {
            // check cache, return data or initial error
            val venueDetailEntity = cacheDatabase.venuesDao.searchVenueDetail(venueId)
            venueDetailEntity?.toData() ?: result.getOrThrow()
        }
    }
}