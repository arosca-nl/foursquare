package com.foursquare.venue.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Category(val name : String, val primary : Boolean)