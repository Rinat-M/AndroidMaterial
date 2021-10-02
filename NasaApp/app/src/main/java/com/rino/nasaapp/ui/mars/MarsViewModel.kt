package com.rino.nasaapp.ui.mars

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rino.nasaapp.entities.RoverCamera
import com.rino.nasaapp.entities.ScreenState
import com.rino.nasaapp.repositories.NasaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MarsViewModel(
    private val nasaRepository: NasaRepository
) : ViewModel() {

    private val _state: MutableLiveData<ScreenState<String>> = MutableLiveData(ScreenState.Loading)
    val state: LiveData<ScreenState<String>> = _state

    fun fetchData(date: String, camera: RoverCamera) {
        viewModelScope.launch(Dispatchers.IO) {
            nasaRepository.getMarsRoverPhotos(date, camera)
                .onSuccess { photosDto ->
                    val firstPhoto = photosDto.photos.first()
                    _state.postValue(ScreenState.Success(firstPhoto.imgSrc))
                }
                .onFailure { _state.postValue(ScreenState.Error(it)) }
        }
    }
}