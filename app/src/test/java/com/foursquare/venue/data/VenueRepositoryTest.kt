package com.foursquare.venue.data

import com.foursquare.venue.api.VenueApi
import com.foursquare.venue.dao.VenueDao
import com.foursquare.venue.dao.VenueEntity
import com.foursquare.venue.dao.toEntity
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class VenueRepositoryTest {

    @Mock
    private lateinit var venueApi: VenueApi

    @Mock
    private lateinit var venueDao: VenueDao

    private val apiSearchResponse = VenueListMetaResponse(VenueListResponse(emptyList()))
    private val dbSearchList = listOf(VenueEntity("id", "name", null, null, null, null))

    private val venueDetail = VenueDetail("id", "name", "description", null, null)
    private val apiDetailResponse = VenueDetailMetaResponse(VenueDetailResponse(venueDetail))
    private val dbDetail = venueDetail.toEntity()

    @Test
    fun `Search first calls API`() = runBlocking {
        whenever(venueApi.search(anyString())).thenReturn(apiSearchResponse)

        val result = runCatching { VenueRepository(venueApi, venueDao).search("test") }

        verify(venueApi).search("test")
        assert(result.isSuccess)
    }

    @Test
    fun `Search success puts data in cache`() = runBlocking {
        whenever(venueApi.search(anyString())).thenReturn(apiSearchResponse)

        val result = runCatching { VenueRepository(venueApi, venueDao).search("test") }

        verify(venueDao).insertVenues(emptyList())
        assert(result.isSuccess)
    }


    @Test
    fun `Search error gets data data from cache`() = runBlocking {
        whenever(venueApi.search(anyString())).thenThrow(RuntimeException("test"))
        whenever(venueDao.search(anyString())).thenReturn(dbSearchList)

        val result = runCatching { VenueRepository(venueApi, venueDao).search("test") }

        verify(venueDao).search("test")
        assert(result.isSuccess)
    }

    @Test
    fun `Search error is returned when cache is empty`() = runBlocking {
        whenever(venueApi.search(anyString())).thenThrow(RuntimeException("error"))
        whenever(venueDao.search(anyString())).thenReturn(emptyList())

        val result = runCatching { VenueRepository(venueApi, venueDao).search("test") }

        assertEquals(result.exceptionOrNull()?.message, "error")
    }

    @Test
    fun `Detail first calls API`() = runBlocking {
        whenever(venueApi.detail(anyString())).thenReturn(apiDetailResponse)

        val result = runCatching { VenueRepository(venueApi, venueDao).detail("test") }

        verify(venueApi).detail("test")
        assertEquals(result.getOrThrow(), venueDetail)
    }

    @Test
    fun `Detail success puts data in cache`() = runBlocking {
        whenever(venueApi.detail(anyString())).thenReturn(apiDetailResponse)

        val result = runCatching { VenueRepository(venueApi, venueDao).detail("test") }

        verify(venueDao).insertVenueDetail(dbDetail)
        assertEquals(result.getOrThrow(), venueDetail)
    }


    @Test
    fun `Detail error gets data data from cache`() = runBlocking {
        whenever(venueApi.detail(anyString())).thenThrow(RuntimeException("test"))
        whenever(venueDao.searchVenueDetail(anyString())).thenReturn(dbDetail)

        val result = runCatching { VenueRepository(venueApi, venueDao).detail("test") }

        verify(venueDao).searchVenueDetail("test")
        assertEquals(result.getOrThrow(), venueDetail)
    }

    @Test
    fun `Detail error is returned when cache is empty`() = runBlocking {
        whenever(venueApi.detail(anyString())).thenThrow(RuntimeException("error"))
        whenever(venueDao.searchVenueDetail(anyString())).thenReturn(null)

        val result = runCatching { VenueRepository(venueApi, venueDao).detail("test") }

        assertEquals(result.exceptionOrNull()?.message, "error")
    }
}