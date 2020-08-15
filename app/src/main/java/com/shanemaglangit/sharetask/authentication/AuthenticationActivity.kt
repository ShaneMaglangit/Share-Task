package com.shanemaglangit.sharetask.authentication

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.util.startMainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        if (sharedPreferences.getBoolean("visited", false)) {
            startMainActivity()
        }
    }
}