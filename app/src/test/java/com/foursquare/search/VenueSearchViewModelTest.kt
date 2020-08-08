package com.foursquare.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.foursquare.MainCoroutineRule
import com.foursquare.lifecycle.ContentEvent
import com.foursquare.lifecycle.ErrorEvent
import com.foursquare.lifecycle.eventContent
import com.foursquare.venue.data.Location
import com.foursquare.venue.data.Venue
import com.foursquare.venue.data.VenueRepository
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class VenueSearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var venueRepository: VenueRepository

    private val venueList =
        listOf(Venue("id", "name", Location("address", "postalCode", "city"), emptyList()))

    @Test
    fun `Get data from repository`() = runBlocking {
        whenever(venueRepository.search(anyString())).thenReturn(venueList)

        val liveData = VenueSearchViewModel(venueRepository).run {
            search("test")
            state
        }

        verify(venueRepository).search("test")
        assertEquals(venueList, eventContent(liveData.value!!))
    }

    @Test
    fun `Pass error from repository`() = runBlocking {
        whenever(venueRepository.search(anyString())).thenThrow(RuntimeException("test"))

        val liveData = VenueSearchViewModel(venueRepository).run {
            search("test")
            state
        }

        verify(venueRepository).search("test")
        assert(liveData.value is ErrorEvent)
    }

    @Test
    fun `Skip repository call for invalid input`() = runBlocking {
        val liveData = VenueSearchViewModel(venueRepository).run {
            search(null)
            state
        }

        verify(venueRepository, never()).search(anyString())
        assert(liveData.value is ContentEvent<*>)
    }
}