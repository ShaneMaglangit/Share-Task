package com.shanemaglangit.sharetask.util

import androidx.lifecycle.MutableLiveData

/**
 * Extension function used to trigger live data for property changes
 */
fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}