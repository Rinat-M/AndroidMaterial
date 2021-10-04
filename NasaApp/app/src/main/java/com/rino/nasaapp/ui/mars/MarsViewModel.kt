package com.rino.nasaapp.ui.mars

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rino.nasaapp.entities.RoverCamera
import com.rino.nasaapp.entities.ScreenState
import com.rino.nasaapp.repositories.NasaRepository
import com.rino.nasaapp.utils.beginOfMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MarsViewModel(
    private val nasaRepository: NasaRepository
) : ViewModel() {

    private val _state: MutableLiveData<ScreenState<String>> = MutableLiveData(ScreenState.Loading)
    val state: LiveData<ScreenState<String>> = _state

    private val _dateFilter: MutableLiveData<Date> = MutableLiveData(Date().beginOfMonth())
    val dateFilter: LiveData<Date> = _dateFilter

    private val _camera: MutableLiveData<RoverCamera> = MutableLiveData(RoverCamera.FHAZ)
    val camera: LiveData<RoverCamera> = _camera

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun fetchData() {
        _state.value = ScreenState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            nasaRepository.getMarsRoverPhotos(
                simpleDateFormat.format(dateFilter.value!!),
                camera.value!!
            )
                .onSuccess { photosDto ->
                    val firstPhoto = photosDto.photos.first()
                    _state.postValue(ScreenState.Success(firstPhoto.imgSrc))
                }
                .onFailure { _state.postValue(ScreenState.Error(it)) }
        }
    }

    fun setCamera(roverCamera: RoverCamera) {
        _camera.value = roverCamera
    }

    fun setDateFilter(date: Date) {
        _dateFilter.value = date
    }
}