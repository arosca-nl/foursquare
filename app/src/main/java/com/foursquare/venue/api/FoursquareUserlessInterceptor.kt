package com.foursquare.venue.api

import com.foursquare.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class FoursquareUserlessInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url: HttpUrl = chain.request().url
            .newBuilder()
            .addEncodedQueryParameter("client_id", BuildConfig.CLIENT_ID)
            .addEncodedQueryParameter("client_secret", BuildConfig.CLIENT_SECRET)
                // freezes API version, date YYYMMDD
            .addEncodedQueryParameter("v", "20200807")
            .build()

        val request: Request = chain.request().newBuilder().url(url).build()

        return chain.proceed(request)
    }
}