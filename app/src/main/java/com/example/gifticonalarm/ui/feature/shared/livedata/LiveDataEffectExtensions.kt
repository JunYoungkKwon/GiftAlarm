package com.example.gifticonalarm.ui.feature.shared.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 단발성 효과(Effect)를 발행한다.
 */
fun <T> MutableLiveData<T?>.emitEffect(effect: T) {
    value = effect
}

/**
 * 단발성 효과(Effect)를 소비 처리한다.
 */
fun <T> MutableLiveData<T?>.consumeEffect() {
    value = null
}

