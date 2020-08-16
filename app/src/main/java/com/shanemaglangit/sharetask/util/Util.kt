package com.shanemaglangit.sharetask.util

import android.app.Activity
import android.content.Intent
import com.shanemaglangit.sharetask.main.MainActivity

fun Activity.startMainActivity() {
    finishAffinity()
    startActivity(Intent(this, MainActivity::class.java))
}