package com.rino.nasaapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rino.nasaapp.R
import com.rino.nasaapp.databinding.SettingsFragmentBinding
import com.rino.nasaapp.entities.Theme
import com.rino.nasaapp.wrappers.ApplyThemeObserver
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initThemeRadioGroup()
    }

    private fun initThemeRadioGroup() {
        val radioButtonId = when (settingsViewModel.currentTheme) {
            null -> R.id.system_theme
            Theme.LIGHT -> R.id.light_theme
            Theme.DARK -> R.id.dark_theme
            Theme.GREY -> R.id.grey_theme
            Theme.BLUE_GREY -> R.id.blue_grey_theme
            Theme.INDIGO -> R.id.indigo_theme
            Theme.LIGHT_GREEN -> R.id.light_green_theme
            Theme.PURPLE -> R.id.purple_theme
        }

        with(binding.themeRadioGroup) {
            check(radioButtonId)

            setOnCheckedChangeListener { _, checkedId ->
                val selectedTheme: Theme? = when (checkedId) {
                    R.id.system_theme -> null
                    R.id.light_theme -> Theme.LIGHT
                    R.id.dark_theme -> Theme.DARK
                    R.id.grey_theme -> Theme.GREY
                    R.id.blue_grey_theme -> Theme.BLUE_GREY
                    R.id.indigo_theme -> Theme.INDIGO
                    R.id.light_green_theme -> Theme.LIGHT_GREEN
                    R.id.purple_theme -> Theme.PURPLE
                    else -> null
                }

                changeTheme(selectedTheme)
            }
        }
    }

    private fun changeTheme(theme: Theme?) {
        settingsViewModel.saveSelectedTheme(theme)
        (activity as ApplyThemeObserver).applyThemeNow()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}