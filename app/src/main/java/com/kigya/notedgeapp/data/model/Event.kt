package com.kigya.notedgeapp.data.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

typealias EventListener<T> = (T) -> Unit
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>
typealias LiveEvent<T> = LiveData<Event<T>>

class Event<T>(
    value: T
) {

    private var _value: T? = value

    fun get(): T? = _value.also { _value = null }

}

fun <T> MutableLiveData<T>.share(): LiveData<T> = this

fun <T> LiveEvent<T>.observeEvent(
    lifecycleOwner: LifecycleOwner,
    listener: EventListener<T>
) {
    this.observe(lifecycleOwner) {
        it?.get()?.let { value ->
            listener(value)
        }
    }
}