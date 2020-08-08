package com.foursquare.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.foursquare.R
import com.foursquare.lifecycle.ContentEvent
import com.foursquare.lifecycle.ErrorEvent
import com.foursquare.lifecycle.LoadingEvent
import com.foursquare.lifecycle.eventContent
import com.foursquare.venue.data.Venue
import com.foursquare.venue.data.VenueDetail
import dagger.hilt.android.AndroidEntryPoint
import io.pixel.android.Pixel
import kotlinx.android.synthetic.main.fragment_venue_detail.*

@AndroidEntryPoint
class VenueDetailDialogFragment : DialogFragment() {

    private val viewModel: VenueDetailViewModel by viewModels()

    companion object {
        private const val VENUE_ID = "venue_id"

        fun newInstance(venue: Venue) =
            VenueDetailDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(VENUE_ID, venue.id)
                }
            }
    }

    override fun getTheme(): Int {
        return R.style.AppTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_venue_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLiveDataObserver()
        arguments?.getString(VENUE_ID)?.let {
            viewModel.loadDetails(it)
        }

        toolbar.setNavigationOnClickListener { dismiss() }
    }

    private fun setupLiveDataObserver() {
        viewModel.state.observe(viewLifecycleOwner, Observer { event ->
            when (event) {
                LoadingEvent -> contentLoadingProgressBar.show()
                is ContentEvent<*> -> {
                    contentLoadingProgressBar.hide()
                    setupView(eventContent(event))
                }
                is ErrorEvent -> {
                    contentLoadingProgressBar.hide()
                    event.throwable?.let {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun setupView(venueDetail: VenueDetail) {
        toolbar.title = venueDetail.name
        venueDetail.rating?.let {
            ratingBar.rating = it
            ratingBar.isVisible = true
        }
        textView.text = venueDetail.description
        venueDetail.bestPhoto?.url?.let {
            Pixel.load(it, imageView)
        }
    }

}