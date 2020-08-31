package com.shanemaglangit.sharetask.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections

open class BaseViewModel : ViewModel() {
    private val _navigationDirection = MutableLiveData<NavDirections>()
    val navigationDirection: LiveData<NavDirections>
        get() = _navigationDirection

    private val _exception = MutableLiveData<Exception>()
    val exception: LiveData<Exception>
        get() = _exception

    fun navigate(direction: NavDirections) {
        _navigationDirection.value = direction
    }

    fun completedNavigation() {
        _navigationDirection.value = null
    }

    fun catchException(exception: Exception) {
        _exception.value = exception
    }

    fun clearException() {
        _exception.value = null
    }
}