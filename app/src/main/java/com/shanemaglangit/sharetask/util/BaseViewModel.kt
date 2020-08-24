package com.shanemaglangit.sharetask.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    private val _navigationAction = MutableLiveData<Int>()
    val navigationAction: LiveData<Int>
        get() = _navigationAction

    private val _exception = MutableLiveData<Exception>()
    val exception: LiveData<Exception>
        get() = _exception

    fun navigate(action: Int) {
        _navigationAction.value = action
    }

    fun completedNavigation() {
        _navigationAction.value = null
    }

    fun catchException(exception: Exception) {
        _exception.value = exception
    }

    fun clearException() {
        _exception.value = null
    }
}