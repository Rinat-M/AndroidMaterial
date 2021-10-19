package com.rino.nasaapp.ui.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rino.nasaapp.entities.ScreenState
import com.rino.nasaapp.entities.Todo
import com.rino.nasaapp.repositories.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TodoListViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _state: MutableLiveData<ScreenState<List<Todo>>> =
        MutableLiveData(ScreenState.Loading)
    val state: LiveData<ScreenState<List<Todo>>> = _state

    private var searchJob: Job? = null

    var selectedTodo: Todo? = null

    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.getTodos()
                .onSuccess { list -> _state.postValue(ScreenState.Success(list)) }
                .onFailure { _state.postValue(ScreenState.Error(it)) }
        }
    }

    fun removeTodo(todo: Todo) {
        todoRepository.removeTodo(todo)
        fetchData()
    }

    fun moveTodo(fromPosition: Int, toPosition: Int) {
        todoRepository.moveTodo(fromPosition, toPosition)
        fetchData()
    }

    fun searchByQuery(query: String?) {
        searchJob?.cancel()

        if (query.isNullOrEmpty()) {
            fetchData()
        } else {
            _state.value = ScreenState.Loading

            searchJob = viewModelScope.launch(Dispatchers.IO) {
                delay(500)
                todoRepository.searchTodo(query)
                    .onSuccess { list -> _state.postValue(ScreenState.Success(list)) }
                    .onFailure { _state.postValue(ScreenState.Error(it)) }
            }
        }
    }

    fun saveTodo(todo: Todo) {
        todoRepository.saveTodo(todo)
        fetchData()
    }

    fun generateTodo() {
        selectedTodo = todoRepository.generateTodo()
    }

}