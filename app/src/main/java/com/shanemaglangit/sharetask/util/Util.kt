package com.shanemaglangit.sharetask.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.shanemaglangit.sharetask.main.MainActivity

fun Activity.startMainActivity(sharedPreferences: SharedPreferences) {
    markIntroAsVisited(sharedPreferences)
    startMainActivity()
}

fun Activity.startMainActivity() {
    finishAffinity()
    startActivity(Intent(this, MainActivity::class.java))
}

fun markIntroAsVisited(sharedPreferences: SharedPreferences) {
    val editor = sharedPreferences.edit()
    editor.putBoolean("visited", true)
    editor.apply()
}