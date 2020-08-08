package com.foursquare.lifecycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

//LiveData that holds but does not broadcast null value. Should not be used when null is a valid event
fun <T> LiveData<T>.nonNull(): LiveData<T> = MediatorLiveData<T>().apply {
    observeForever {} // makes addSource sync with source before Observers are added
    addSource(this@nonNull) { if (it != null) value = it }
}