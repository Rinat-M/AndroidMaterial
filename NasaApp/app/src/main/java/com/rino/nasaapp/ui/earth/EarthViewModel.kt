package com.rino.nasaapp.ui.earth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rino.nasaapp.entities.ScreenState
import com.rino.nasaapp.repositories.NasaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class EarthViewModel(
    private val nasaRepository: NasaRepository
) : ViewModel() {

    private val _state: MutableLiveData<ScreenState<String>> = MutableLiveData(ScreenState.Loading)
    val state: LiveData<ScreenState<String>> = _state

    fun fetchData(date: Date = Date()) {
        viewModelScope.launch(Dispatchers.IO) {
            nasaRepository.getEpicImageLink(date)
                .onSuccess { _state.postValue(ScreenState.Success(it)) }
                .onFailure { _state.postValue(ScreenState.Error(it)) }
        }
    }
}