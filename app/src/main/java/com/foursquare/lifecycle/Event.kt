package com.foursquare.lifecycle

sealed class LiveDataEvent

object LoadingEvent : LiveDataEvent()

data class ContentEvent<T>(val content: T) : LiveDataEvent()

data class ErrorEvent(private val _throwable: Throwable?) : LiveDataEvent() {
    private var hasBeenHandled = false

    val throwable: Throwable?
        get() = if (hasBeenHandled) null else _throwable
            .also {
                hasBeenHandled = true
            }
}

@Suppress("UNCHECKED_CAST")
inline fun <reified E> eventContent(event: LiveDataEvent) =
    (event as? ContentEvent<Any>)?.content as E