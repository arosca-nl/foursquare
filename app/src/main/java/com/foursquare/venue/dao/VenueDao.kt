package com.foursquare.venue.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VenueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVenues(venues: List<VenueEntity>)

    @Query("SELECT * FROM VenueEntity WHERE city LIKE :city")
    fun search(city: String): List<VenueEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVenueDetail(venueDetail: VenueDetailEntity)

    @Query("SELECT * FROM VenueDetailEntity WHERE id == :venueId")
    fun searchVenueDetail(venueId: String): VenueDetailEntity?
}