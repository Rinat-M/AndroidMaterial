package com.rino.nasaapp.ui.apod

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rino.nasaapp.entities.ScreenState
import com.rino.nasaapp.remote.entities.ApodDTO
import com.rino.nasaapp.repositories.NasaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApodViewModel(
    private val nasaRepository: NasaRepository
) : ViewModel() {

    companion object {
        private const val TAG = "ApodViewModel"
    }

    private val _state: MutableLiveData<ScreenState<ApodDTO>> =
        MutableLiveData(ScreenState.Loading)
    val state: LiveData<ScreenState<ApodDTO>> = _state

    fun fetchData(date: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            nasaRepository.getAstronomyPictureOfTheDay(date)
                .onSuccess { it?.let { apodDTO -> _state.postValue(ScreenState.Success(apodDTO)) } }
                .onFailure { _state.postValue(ScreenState.Error(it)) }
        }
    }

}