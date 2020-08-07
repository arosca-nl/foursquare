package com.foursquare.venue.api

import com.foursquare.venue.data.VenueDetailMetaResponse
import com.foursquare.venue.data.VenueListMetaResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VenueApi {

    @GET("/v2/venues/search?radius=1000&limit=10")
    suspend fun search(@Query("near") near: String): VenueListMetaResponse

    @GET("v2/venues/{id}")
    suspend fun detail(@Path("id") venueId: String): VenueDetailMetaResponse
}