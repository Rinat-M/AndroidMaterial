package com.rino.nasaapp.ui.apod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rino.nasaapp.databinding.ApodFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ApodFragment : Fragment() {

    companion object {
        fun newInstance() = ApodFragment()
    }

    private val apodViewModel: ApodViewModel by viewModel()

    private var _binding: ApodFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ApodFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}