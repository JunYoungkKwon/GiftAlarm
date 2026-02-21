package com.example.gifticonalarm.ui.feature.shared.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * 단일 값을 가지는 LiveData를 생성한다.
 */
fun <T> liveDataOf(value: T): LiveData<T> {
    return MutableLiveData(value)
}

/**
 * null 값을 가지는 LiveData를 생성한다.
 */
fun <T> nullLiveData(): LiveData<T?> {
    return MutableLiveData<T?>(null)
}

