package com.foursquare.venue.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VenueDetail(
    val name: String,
    val description: String?,
    val rating: Float?,
    val bestPhoto: Photo?
)