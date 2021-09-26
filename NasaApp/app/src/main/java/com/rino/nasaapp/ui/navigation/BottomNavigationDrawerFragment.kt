package com.rino.nasaapp.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rino.nasaapp.R
import com.rino.nasaapp.databinding.BottomNavigationLayoutBinding
import com.rino.nasaapp.utils.showToast

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    companion object {
        const val FRAGMENT_TAG = "BottomNavigationDrawerFragment"
    }

    private var _binding: BottomNavigationLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomNavigationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_one -> {
                    requireContext().showToast(R.string.favorite)
                    dismiss()
                }
                R.id.navigation_two -> {
                    requireContext().showToast(R.string.settings)
                    dismiss()
                }
            }
            true
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}

