package com.darf.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), IBottomNavViewCallback {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        navController = Navigation.findNavController(this, R.id.main_container)
        navController.navigate(R.id.loginFragment)
    }

    override fun runBottomNavigationView(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_messenger -> R.id.messengerFragment
                R.id.nav_users -> R.id.usersFragment
                R.id.nav_profile -> R.id.profileFragment
                else -> null
            }

            navController.navigate(fragment ?: R.id.loginFragment)

            true
        }
    }
}