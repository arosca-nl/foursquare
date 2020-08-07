package com.foursquare.venue.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VenueListMetaResponse(val response: VenueListResponse) {
    val venues = response.venues
}

@JsonClass(generateAdapter = true)
data class VenueListResponse(val venues: List<Venue>)