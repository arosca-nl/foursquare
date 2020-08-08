package com.foursquare.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.foursquare.R
import com.foursquare.lifecycle.nonNull
import com.foursquare.venue.data.Venue

class VenueAdapter(private val layoutInflater: LayoutInflater) :
    RecyclerView.Adapter<VenueHolder>() {

    private val mutableSelectedVenue = MutableLiveData<Venue>()
    private val mutableVenues = mutableListOf<Venue>()

    var venues: List<Venue>
        get() = mutableVenues
        set(list) {
            mutableVenues.clear()
            mutableVenues.addAll(list)
            notifyDataSetChanged()
        }

    val selectedVenue: LiveData<Venue> = mutableSelectedVenue.nonNull()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueHolder =
        VenueHolder(layoutInflater.inflate(R.layout.row_venue, parent, false))

    override fun onBindViewHolder(holder: VenueHolder, position: Int) {
        val venue = mutableVenues[position]
        holder.itemView.setOnClickListener { mutableSelectedVenue.value = venue }
        holder.bind(venue)
    }

    override fun getItemCount(): Int = mutableVenues.size
}