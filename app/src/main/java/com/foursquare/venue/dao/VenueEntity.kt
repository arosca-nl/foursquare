package com.foursquare.venue.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.foursquare.venue.data.Category
import com.foursquare.venue.data.Location
import com.foursquare.venue.data.Venue

@Entity
data class VenueEntity(
    @PrimaryKey val id: String,
    val name: String,
    val address: String?,
    val postalCode: String?,
    val city: String?,
    val categoryName: String?
)

fun Venue.toEntity() =
    VenueEntity(id, name, location.address, location.postalCode, location.city, categoryName)

fun List<Venue>.toEntity() = map { it.toEntity() }

fun VenueEntity.toData() = if (categoryName.isNullOrEmpty())
    Venue(id, name, Location(address, postalCode, city), emptyList())
else
    Venue(id, name, Location(address, postalCode, city), listOf(Category(categoryName, true)))

fun List<VenueEntity>.toData() = map { it.toData() }