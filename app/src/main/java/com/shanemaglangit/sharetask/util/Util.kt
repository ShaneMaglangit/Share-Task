package com.shanemaglangit.sharetask.util

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.shanemaglangit.sharetask.main.MainActivity

fun Activity.startMainActivity() {
    finishAffinity()
    startActivity(Intent(this, MainActivity::class.java))
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}