package com.foursquare.venue.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    val address: String?,
    val postalCode: String?,
    val city: String?
) {
    val formattedAddress by lazy {
        listOf(address, postalCode, city)
            .filter { !it.isNullOrEmpty() }
            .joinToString(separator = "\n")
    }
}