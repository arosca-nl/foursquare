package com.foursquare.venue.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [VenueEntity::class, VenueDetailEntity::class], version = 1)
abstract class CacheDatabase : RoomDatabase() {

    abstract val venuesDao: VenuesDao

}