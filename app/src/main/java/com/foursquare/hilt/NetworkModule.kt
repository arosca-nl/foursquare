package com.foursquare.hilt

import com.foursquare.venue.api.FoursquareInterceptor
import com.foursquare.venue.api.VenueApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(FoursquareInterceptor())
        .build()

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://api.foursquare.com")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    fun provideVenueApi(retrofit: Retrofit): VenueApi = retrofit
        .create(VenueApi::class.java)

}