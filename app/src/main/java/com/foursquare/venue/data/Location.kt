package com.foursquare.venue.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(val formattedAddress : List<String>) {
    val address by lazy { formattedAddress.joinToString(separator = "\n") }
}