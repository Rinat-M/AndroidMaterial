package com.rino.nasaapp.ui.earth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rino.nasaapp.entities.ScreenState
import com.rino.nasaapp.repositories.NasaRepository
import com.rino.nasaapp.utils.beginOfMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class EarthViewModel(
    private val nasaRepository: NasaRepository
) : ViewModel() {

    private val _state: MutableLiveData<ScreenState<String>> = MutableLiveData(ScreenState.Loading)
    val state: LiveData<ScreenState<String>> = _state

    private val _dateFilter: MutableLiveData<Date> = MutableLiveData(Date().beginOfMonth())
    val dateFilter: LiveData<Date> = _dateFilter

    fun fetchData() {
        _state.value = ScreenState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            dateFilter.value?.let { date ->
                nasaRepository.getEpicImageLink(date)
                    .onSuccess { _state.postValue(ScreenState.Success(it)) }
                    .onFailure { _state.postValue(ScreenState.Error(it)) }
            }
        }
    }

    fun setDateFilter(date: Date) {
        _dateFilter.value = date
    }
}