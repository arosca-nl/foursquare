package com.foursquare.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.foursquare.MainCoroutineRule
import com.foursquare.lifecycle.ErrorEvent
import com.foursquare.lifecycle.eventContent
import com.foursquare.venue.data.VenueDetail
import com.foursquare.venue.data.VenueRepository
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class VenueDetailViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var venueRepository: VenueRepository

    private val venueDetail = VenueDetail("id", "name", "description", null, null)

    @Test
    fun `Get data from repository`() = runBlocking {
        whenever(venueRepository.detail(ArgumentMatchers.anyString())).thenReturn(venueDetail)

        val liveData = VenueDetailViewModel(venueRepository).run {
            loadDetails("test")
            state
        }

        verify(venueRepository).detail("test")
        assertEquals(venueDetail, eventContent(liveData.value!!))
    }

    @Test
    fun `Pass error from repository`() = runBlocking {
        whenever(venueRepository.detail(ArgumentMatchers.anyString())).thenThrow(RuntimeException("test"))

        val liveData = VenueDetailViewModel(venueRepository).run {
            loadDetails("test")
            state
        }

        verify(venueRepository).detail("test")
        assert(liveData.value is ErrorEvent)
    }
}