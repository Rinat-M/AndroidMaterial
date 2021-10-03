package com.rino.nasaapp.ui.mars

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.rino.nasaapp.R
import com.rino.nasaapp.databinding.MarsFragmentBinding
import com.rino.nasaapp.databinding.ProgressBarAndErrorMsgBinding
import com.rino.nasaapp.entities.RoverCamera
import com.rino.nasaapp.entities.ScreenState
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        marsViewModel.fetchData()
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

        subscribeOn()

        initDatePicker()
        initChips()
    }

    private fun subscribeOn() {
        with(marsViewModel) {
            dateFilter.observe(viewLifecycleOwner) { date ->
                date?.let { binding.dateFilter.text = simpleDateFormat.format(it) }
            }

            camera.observe(viewLifecycleOwner) { camera ->
                camera?.let {
                    val chipId = getChipIdByRoverCamera(it)
                    changeCameraDescriptionByChipId(chipId)
                }
            }

            state.observe(viewLifecycleOwner) { state ->
                state?.let { processData(it) }
            }
        }
    }

    private fun changeCameraDescriptionByChipId(chipId: Int) {
        with(binding) {
            when (chipId) {
                frontCameraChip.id -> {
                    cameraDescription.text =
                        context?.getText(R.string.front_camera_description) ?: ""
                }
                rearCameraChip.id -> {
                    cameraDescription.text =
                        context?.getText(R.string.rear_camera_description) ?: ""
                }
                chemistryCameraChip.id -> {
                    cameraDescription.text =
                        context?.getText(R.string.chemistry_camera_description) ?: ""
                }
                mastCameraChip.id -> {
                    cameraDescription.text =
                        context?.getText(R.string.mast_camera_description) ?: ""
                }

                else -> {
                }
            }
        }
    }

    private fun initChips() {
        with(binding) {
            camerasChipGroup.setOnCheckedChangeListener { _, checkedId ->
                val camera = getRoverCameraByChipId(checkedId)

                with(marsViewModel) {
                    setCamera(camera)
                    fetchData()
                }
            }
        }
    }

    private fun initDatePicker() {
        binding.dateFilter.setOnClickListener {
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                calendar.set(year, month, day)

                with(marsViewModel) {
                    setDateFilter(Date(calendar.timeInMillis))
                    fetchData()
                }
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

    private fun getRoverCameraByChipId(@IdRes chipId: Int) = when (chipId) {
        binding.frontCameraChip.id -> RoverCamera.FHAZ
        binding.rearCameraChip.id -> RoverCamera.RHAZ
        binding.chemistryCameraChip.id -> RoverCamera.CHEMCAM
        binding.mastCameraChip.id -> RoverCamera.MAST

        else -> throw Exception("Unknown rover camera")
    }

    private fun getChipIdByRoverCamera(camera: RoverCamera) = when (camera) {
        RoverCamera.FHAZ -> binding.frontCameraChip.id
        RoverCamera.RHAZ -> binding.rearCameraChip.id
        RoverCamera.MAST -> binding.chemistryCameraChip.id
        RoverCamera.CHEMCAM -> binding.mastCameraChip.id
    }

    override fun onDestroy() {
        _binding = null
        _includeBinding = null
        super.onDestroy()
    }

}