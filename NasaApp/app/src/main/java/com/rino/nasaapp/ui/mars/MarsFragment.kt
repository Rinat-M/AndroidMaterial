package com.rino.nasaapp.ui.mars

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.rino.nasaapp.R
import com.rino.nasaapp.databinding.MarsFragmentBinding
import com.rino.nasaapp.databinding.ProgressBarAndErrorMsgBinding
import com.rino.nasaapp.entities.RoverCamera
import com.rino.nasaapp.entities.ScreenState
import com.rino.nasaapp.utils.beginOfMonth
import com.rino.nasaapp.utils.showSnackBar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MarsFragment : Fragment() {

    companion object {
        fun newInstance() = MarsFragment()
    }

    private val marsViewModel: MarsViewModel by viewModel()

    private var _binding: MarsFragmentBinding? = null
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

    private val calendar = Calendar.getInstance()
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private var dateFilter = Date().beginOfMonth()
    private var cameraFilter = RoverCamera.FHAZ

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MarsFragmentBinding.inflate(layoutInflater, container, false)
        _includeBinding = ProgressBarAndErrorMsgBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dateFilter.text = simpleDateFormat.format(dateFilter)

        marsViewModel.state.observe(viewLifecycleOwner) { state ->
            state?.let { processData(it) }
        }

        initDatePicker()
        initChips()
    }

    private fun fetchData() =
        marsViewModel.fetchData(simpleDateFormat.format(dateFilter), cameraFilter)

    private fun initChips() {
        with(binding) {
            camerasChipGroup.setOnCheckedChangeListener { _, checkedId ->
                cameraFilter = when (checkedId) {
                    frontCameraChip.id -> {
                        cameraDescription.text =
                            context?.getText(R.string.front_camera_description) ?: ""
                        RoverCamera.FHAZ
                    }
                    rearCameraChip.id -> {
                        cameraDescription.text =
                            context?.getText(R.string.rear_camera_description) ?: ""
                        RoverCamera.RHAZ
                    }
                    chemistryCameraChip.id -> {
                        cameraDescription.text =
                            context?.getText(R.string.chemistry_camera_description) ?: ""
                        RoverCamera.CHEMCAM
                    }
                    mastCameraChip.id -> {
                        cameraDescription.text =
                            context?.getText(R.string.mast_camera_description) ?: ""
                        RoverCamera.MAST
                    }

                    else -> throw Exception("Unknown rover camera")
                }

                fetchData()
            }
        }
    }

    private fun initDatePicker() {
        binding.dateFilter.setOnClickListener {
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                calendar.set(year, month, day)
                dateFilter = Date(calendar.timeInMillis)

                fetchData()

                binding.dateFilter.text = simpleDateFormat.format(dateFilter)
            }

            val dialog = DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            dialog.show()
        }
    }

    private fun processData(state: ScreenState<String>) {
        when (state) {
            ScreenState.Loading -> {
                binding.visibilityGroup.isVisible = false
                includeBinding.progressBar.isVisible = true
                includeBinding.errorMsg.isVisible = false
            }

            is ScreenState.Success -> {
                with(binding) {
                    visibilityGroup.isVisible = true
                    includeBinding.progressBar.isVisible = false
                    includeBinding.errorMsg.isVisible = false

                    Glide.with(requireContext())
                        .load(state.data)
                        .placeholder(circularProgressDrawable)
                        .error(R.drawable.ic_image)
                        .into(image)

                    container.showSnackBar("url: ${state.data}")
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

    override fun onDestroy() {
        _binding = null
        _includeBinding = null
        super.onDestroy()
    }

}