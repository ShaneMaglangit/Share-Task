package com.shanemaglangit.sharetask.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections

open class BaseViewModel : ViewModel() {
    /**
     * Live data for observing navigation actions
     */
    private val _navigationDirection = MutableLiveData<NavDirections>()
    val navigationDirection: LiveData<NavDirections>
        get() = _navigationDirection

    /**
     * Live data for observing exceptions
     */
    private val _exception = MutableLiveData<Exception>()
    val exception: LiveData<Exception>
        get() = _exception

    /**
     * Used to update the live data for navigation with the proper
     * navigation action / direction
     */
    fun navigate(direction: NavDirections) {
        _navigationDirection.value = direction
    }

    /**
     * Used to reset the live data once the navigation is successfully handled
     */
    fun completedNavigation() {
        _navigationDirection.value = null
    }

    /**
     * Used to clear the exception live data once it is successfully handled
     */
    fun catchException(exception: Exception) {
        _exception.value = exception
    }

    fun clearException() {
        _exception.value = null
    }
}