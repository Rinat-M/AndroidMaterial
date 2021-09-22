package com.rino.nasaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rino.nasaapp.ui.apod.ApodFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ApodFragment.newInstance())
                    .commitNow()
        }
    }
}