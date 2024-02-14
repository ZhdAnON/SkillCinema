package com.zhdanon.skillcinema.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.navigation.fragment.NavHostFragment
import com.zhdanon.skillcinema.R
import com.zhdanon.skillcinema.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "ZhoraTAG"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val navController = navHost.navController

        navController.navigate(R.id.fragmentTopCollections)

        PreferenceManager.getDefaultSharedPreferences(this).apply {
            if (!getBoolean(PREFERENCES_NAME, false)) {
                navController.navigate(R.id.fragmentOnboarding)
            } else {
                navController.navigate(R.id.fragmentTopCollections)
            }
        }
    }

    companion object {
        const val PREFERENCES_NAME = "pref_name"
    }
}