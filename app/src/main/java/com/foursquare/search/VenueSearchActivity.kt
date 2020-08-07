package com.foursquare.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.foursquare.R
import com.foursquare.venue.data.Venue
import com.foursquare.search.ui.VenueAdapter
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
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean  {
                contentLoadingProgressBar.show()
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
                val lifecycleOwner = this@VenueSearchActivity
                viewModel.venues.observe(lifecycleOwner, Observer<VenuesResult> {
                    venues = it.getOrDefault(emptyList())
                    contentLoadingProgressBar.hide()
                })
                selectedVenue.observe(lifecycleOwner, Observer<Venue> { onSelectedVenue(it) })
            }
        }
    }

    private fun onSelectedVenue(venue: Venue) {
        Toast.makeText(this, venue.name, Toast.LENGTH_SHORT).show()
    }
}