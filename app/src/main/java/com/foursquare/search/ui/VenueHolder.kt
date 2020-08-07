package com.foursquare.search.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.foursquare.R
import com.foursquare.venue.data.Venue

class VenueHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(venue: Venue) {
        val context = itemView.context

        (itemView as TextView).text = if (venue.categoryName == null)
            context.getString(
                R.string.venue_name_location,
                venue.name,
                venue.location.address
            )
        else
            context.getString(
                R.string.venue_name_category_location,
                venue.name,
                venue.categoryName,
                venue.location.address
            )
    }

}