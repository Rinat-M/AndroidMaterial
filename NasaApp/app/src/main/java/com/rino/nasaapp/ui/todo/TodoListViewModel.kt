package com.rino.nasaapp.ui.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rino.nasaapp.entities.ScreenState
import com.rino.nasaapp.entities.Todo
import com.rino.nasaapp.repositories.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoListViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _state: MutableLiveData<ScreenState<List<Todo>>> =
        MutableLiveData(ScreenState.Loading)
    val state: LiveData<ScreenState<List<Todo>>> = _state

    fun fetchData() {
        _state.value = ScreenState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.getTodos()
                .onSuccess { list -> _state.postValue(ScreenState.Success(list)) }
                .onFailure { _state.postValue(ScreenState.Error(it)) }
        }
    }
}