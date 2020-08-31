package com.shanemaglangit.sharetask.main

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.shanemaglangit.sharetask.R
import com.shanemaglangit.sharetask.databinding.ActivityMainBinding
import com.shanemaglangit.sharetask.util.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNavMain, navHost.navController)
    }
}