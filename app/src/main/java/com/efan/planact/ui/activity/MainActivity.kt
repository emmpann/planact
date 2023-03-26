package com.efan.planact.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.efan.planact.ui.fragments.addTask.CreatetaskFragment
import com.efan.planact.ui.fragments.calendar.CalendarFragment
import com.efan.planact.ui.fragments.home.HomeFragment
import com.efan.planact.R
import com.efan.planact.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigation
        val navController = findNavController(R.id.fragment_container)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.bottomNavigation.visibility = VISIBLE
                }
                R.id.createtaskFragment -> {
                    binding.bottomNavigation.visibility = VISIBLE
                }
                R.id.calendarFragment -> {
                    binding.bottomNavigation.visibility = VISIBLE
                }
                else -> {
                    binding.bottomNavigation.visibility = GONE
                }
            }
        }

        bottomNavigationView.setupWithNavController(navController)
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}