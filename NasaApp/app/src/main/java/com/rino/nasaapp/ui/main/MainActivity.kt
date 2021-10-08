package com.rino.nasaapp.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.android.material.bottomappbar.BottomAppBar
import com.rino.nasaapp.R
import com.rino.nasaapp.databinding.MainActivityBinding
import com.rino.nasaapp.entities.Screen
import com.rino.nasaapp.entities.Theme
import com.rino.nasaapp.ui.apod.ApodFragment
import com.rino.nasaapp.ui.navigation.BottomNavigationDrawerFragment
import com.rino.nasaapp.ui.settings.SettingsFragment
import com.rino.nasaapp.utils.showToast
import com.rino.nasaapp.wrappers.ApplyThemeObserver
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), ApplyThemeObserver {

    private lateinit var binding: MainActivityBinding

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()

        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.bottomAppBar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ApodFragment.newInstance())
                .commitNow()
        }

        mainViewModel.screen.observe(this) { currentScreen ->
            currentScreen?.let { configureBottomAppBar(it) }
        }
    }

    override fun applyTheme() {
        when (val currentTheme = mainViewModel.currentTheme) {
            null -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            Theme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                setTheme(currentTheme.themeId)
            }
        }
    }

    override fun applyThemeNow() {
        recreate()
    }

    private fun configureBottomAppBar(currentScreen: Screen) {
        when (currentScreen) {
            Screen.MAIN -> {
                with(binding.fab) {
                    val mainIcon =
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_light_bulb)
                    setImageDrawable(mainIcon)
                    setOnClickListener(null)
                }

                with(binding.bottomAppBar) {
                    navigationIcon =
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_menu)
                    fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                }
            }

            Screen.SECONDARY -> {
                with(binding.bottomAppBar) {
                    navigationIcon = null
                    fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                }

                with(binding.fab) {
                    val replyIcon =
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_reply)
                    setImageDrawable(replyIcon)
                    setOnClickListener { onBackPressed() }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite_menu, R.id.nav_favorite -> {
                showToast(R.string.favorite)
                true
            }

            R.id.settings_menu, R.id.nav_settings -> {
                val fragment = supportFragmentManager.findFragmentByTag(SettingsFragment.TAG)
                val isFragmentAlreadyExist = fragment != null

                val ft = supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out
                    )
                    .replace(
                        R.id.container,
                        fragment ?: SettingsFragment.newInstance(),
                        SettingsFragment.TAG
                    )

                if (!isFragmentAlreadyExist) {
                    mainViewModel.changeScreen(Screen.SECONDARY)
                    ft.addToBackStack(null)
                }

                ft.commit()

                true
            }

            android.R.id.home -> {
                BottomNavigationDrawerFragment().show(
                    supportFragmentManager,
                    BottomNavigationDrawerFragment.TAG
                )

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        mainViewModel.changeScreen(Screen.MAIN)
        super.onBackPressed()
    }
}

