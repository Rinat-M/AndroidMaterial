package com.rino.nasaapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.rino.nasaapp.R
import com.rino.nasaapp.databinding.MainActivityBinding
import com.rino.nasaapp.entities.Theme
import com.rino.nasaapp.ui.apod.ApodFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applyTheme()

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.bottomAppBar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ApodFragment.newInstance())
                .commitNow()
        }
    }

    private fun applyTheme() {
        val currentTheme = mainViewModel.currentTheme

        if (currentTheme == Theme.DARK) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            currentTheme?.let { setTheme(it.themeId) }
        }
    }

}