package com.foursquare.search

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.foursquare.R
import com.foursquare.detail.VenueDetailDialogFragment
import com.foursquare.lifecycle.ContentEvent
import com.foursquare.lifecycle.ErrorEvent
import com.foursquare.lifecycle.LoadingEvent
import com.foursquare.lifecycle.eventContent
import com.foursquare.search.ui.VenueAdapter
import com.foursquare.venue.data.Venue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_venue_search.*


@AndroidEntryPoint
class VenueSearchActivity : AppCompatActivity() {

    private val viewModel: VenueSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_search)

        setupSearchView()
        setupRecyclerView()
    }

    private fun setupSearchView() {
        searchView.onActionViewExpanded()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                viewModel.search(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            adapter = VenueAdapter(layoutInflater).apply {
                setupLiveDataObserver(this)
            }
        }
    }

    private fun setupLiveDataObserver(adapter: VenueAdapter) {
        // data in from model
        viewModel.state.observe(this, Observer { event ->
            when (event) {
                LoadingEvent -> contentLoadingProgressBar.show()
                is ContentEvent<*> -> {
                    contentLoadingProgressBar.hide()
                    adapter.venues = eventContent(event)
                }
                is ErrorEvent -> {
                    contentLoadingProgressBar.hide()
                    event.throwable?.let {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        // data out from click
        adapter.selectedVenue.observe(this, Observer<Venue> { onSelectedVenue(it) })
    }

    private fun onSelectedVenue(venue: Venue) {
        VenueDetailDialogFragment.newInstance(venue)
            .show(supportFragmentManager, "VenueDetailDialogFragment")
    }
}