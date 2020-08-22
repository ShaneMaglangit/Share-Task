package com.shanemaglangit.sharetask.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BaseViewModel : ViewModel() {
    private val _navigationAction = MutableLiveData<Int>()
    val navigationAction: LiveData<Int>
        get() = _navigationAction

    private val _exception = MutableLiveData<Exception>()
    val exception: LiveData<Exception>
        get() = _exception


}