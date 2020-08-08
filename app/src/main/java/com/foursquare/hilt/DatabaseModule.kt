package com.foursquare.hilt

import android.content.Context
import androidx.room.Room
import com.foursquare.venue.dao.CacheDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    fun provideCacheDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            CacheDatabase::class.java, "venue-cache"
        ).build()

    @Provides
    @Singleton
    fun provideVenuesDao(cacheDatabase: CacheDatabase) = cacheDatabase.venueDao
}