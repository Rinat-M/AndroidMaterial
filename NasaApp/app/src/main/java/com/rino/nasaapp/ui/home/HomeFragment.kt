package com.rino.nasaapp.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.rino.nasaapp.R
import com.rino.nasaapp.databinding.HomeFragmentBinding
import com.rino.nasaapp.databinding.ProgressBarAndErrorMsgBinding
import com.rino.nasaapp.entities.DateParameter
import com.rino.nasaapp.entities.ScreenState
import com.rino.nasaapp.remote.entities.ApodDTO
import com.rino.nasaapp.utils.getSpannableWithHighlightedWords
import com.rino.nasaapp.utils.searchInWikipedia
import com.rino.nasaapp.utils.showSnackBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val homeViewModel: HomeViewModel by viewModel()

    private var _binding: HomeFragmentBinding? = null
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
        homeViewModel.fetchData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        _includeBinding = ProgressBarAndErrorMsgBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.state.observe(viewLifecycleOwner) { state ->
            state?.let { processData(it) }
        }

        with(binding) {
            inputLayout.setEndIconOnClickListener { requireContext().searchInWikipedia(inputEditText.text.toString()) }

            inputEditText.setOnEditorActionListener { textView, actionId, _ ->
                val searchText = textView.text.toString()

                if (actionId == EditorInfo.IME_ACTION_GO && searchText.isNotEmpty()) {
                    requireContext().searchInWikipedia(searchText)
                    true
                } else {
                    false
                }
            }

            datesChipGroup.setOnCheckedChangeListener { _, checkedId ->
                homeViewModel.dateParameter = when (checkedId) {
                    yesterdayChip.id -> DateParameter.YESTERDAY
                    beforeYesterdayChip.id -> DateParameter.DAY_BEFORE_YESTERDAY
                    else -> DateParameter.TODAY
                }

                homeViewModel.fetchData()
            }
        }
    }

    private fun processData(state: ScreenState<ApodDTO>) {
        when (state) {
            ScreenState.Loading -> {
                binding.visibilityGroup.isVisible = false
                includeBinding.progressBar.isVisible = true
                includeBinding.errorMsg.isVisible = false
            }

            is ScreenState.Success -> {
                val apodData = state.data

                with(binding) {
                    visibilityGroup.isVisible = true
                    includeBinding.progressBar.isVisible = false
                    includeBinding.errorMsg.isVisible = false

                    Glide.with(requireContext())
                        .load(apodData.url)
                        .placeholder(circularProgressDrawable)
                        .error(R.drawable.ic_network_error)
                        .into(apodImage)

                    apodCoordinatorLayout.showSnackBar("url: ${apodData.url}")

                    currentApodTitle.text = apodData.title

                    val wordsToHighlight =
                        listOf("fireball", "meteor", "moon", "earth", "solar", "system", "astro")

                    currentApodExplanation.text = apodData.explanation
                        .getSpannableWithHighlightedWords(wordsToHighlight, Color.RED)
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