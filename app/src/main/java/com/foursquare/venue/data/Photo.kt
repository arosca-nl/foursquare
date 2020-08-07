package com.foursquare.venue.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Photo(val prefix: String, val suffix: String) {
    val url by lazy { prefix + "original" + suffix }
}