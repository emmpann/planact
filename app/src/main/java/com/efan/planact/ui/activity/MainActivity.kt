package com.efan.planact.ui.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.efan.planact.R
import com.efan.planact.databinding.ActivityMainBinding
import com.efan.planact.util.PreferencesManager
import com.efan.planact.util.SharedPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferencesManager = PreferencesManager(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container)!!
        val navController = navHost.findNavController()
        val bottomNavigationView = binding.bottomNavigation

//        Handler().postDelayed({
//            if(preferencesManager.isLogin()) {
//                SharedPreferences.saveUserId(preferencesManager.getId())
//                navController.navigate(R.id.action_onboardingFragment_to_homeFragment)
//            } else {
//                navController.navigate(R.id.action_onboardingFragment_to_loginFragment)
//            }
//        }, 2000)

        if(preferencesManager.isLogin()) {
            SharedPreferences.saveUserId(preferencesManager.getId())
            navController.navigate(R.id.action_onboardingFragment_to_homeFragment)
        } else {
            navController.navigate(R.id.action_onboardingFragment_to_loginFragment)
        }

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
}