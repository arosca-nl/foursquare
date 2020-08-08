package com.foursquare.venue.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.foursquare.venue.data.Photo
import com.foursquare.venue.data.VenueDetail

@Entity
data class VenueDetailEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String?,
    val rating: Float?,
    val photoUrlPrefix: String?,
    val photoUrlSuffix: String?
)

fun VenueDetail.toEntity() =
    VenueDetailEntity(id, name, description, rating, bestPhoto?.prefix, bestPhoto?.suffix)

fun VenueDetailEntity.toData() = if (photoUrlPrefix == null || photoUrlSuffix == null)
    VenueDetail(id, name, description, rating, null)
else
    VenueDetail(id, name, description, rating, Photo(photoUrlPrefix, photoUrlSuffix))