package com.foursquare.venue.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Venue(
    val id: String,
    val name: String,
    val location: Location,
    val categories: List<Category>
) {

    val categoryName by lazy { categories.firstOrNull { it.primary }?.name }
}