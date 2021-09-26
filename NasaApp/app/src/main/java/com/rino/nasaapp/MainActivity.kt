package com.rino.nasaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rino.nasaapp.databinding.MainActivityBinding
import com.rino.nasaapp.ui.apod.ApodFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.bottomAppBar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ApodFragment.newInstance())
                .commitNow()
        }
    }
}