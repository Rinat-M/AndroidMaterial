package com.rino.nasaapp.ui.apod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.rino.nasaapp.R
import com.rino.nasaapp.databinding.ApodFragmentBinding
import com.rino.nasaapp.databinding.ProgressBarAndErrorMsgBinding
import com.rino.nasaapp.entities.ScreenState
import com.rino.nasaapp.remote.entities.ApodDTO
import org.koin.androidx.viewmodel.ext.android.viewModel

class ApodFragment : Fragment() {

    companion object {
        fun newInstance() = ApodFragment()
    }

    private val apodViewModel: ApodViewModel by viewModel()

    private var _binding: ApodFragmentBinding? = null
    private val binding get() = _binding!!

    private var _includeBinding: ProgressBarAndErrorMsgBinding? = null
    private val includeBinding get() = _includeBinding!!

    private val circularProgressDrawable by lazy {
        CircularProgressDrawable(requireContext()).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apodViewModel.fetchData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ApodFragmentBinding.inflate(inflater, container, false)
        _includeBinding = ProgressBarAndErrorMsgBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apodViewModel.state.observe(viewLifecycleOwner) { state ->
            state?.let { processData(it) }
        }
    }

    private fun processData(state: ScreenState<ApodDTO>) {
        when (state) {
            ScreenState.Loading -> {
                binding.visibilityGroup.isVisible = false
                includeBinding.progressBar.isVisible = true
            }

            is ScreenState.Success -> {
                with(binding) {
                    visibilityGroup.isVisible = true
                    includeBinding.progressBar.isVisible = false

                    Glide.with(requireContext())
                        .load(state.data.url)
                        .placeholder(circularProgressDrawable)
                        .error(R.drawable.ic_image)
                        .into(apodImage)

                    currentApodTitle.text = state.data.title
                    currentApodExplanation.text = state.data.explanation
                }
            }

            is ScreenState.Error -> {
                binding.visibilityGroup.isVisible = false

                with(includeBinding) {
                    progressBar.isVisible = false
                    errorMsg.isVisible = true
                    errorMsg.text = state.error.toString()
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        _includeBinding = null
        super.onDestroyView()
    }
}