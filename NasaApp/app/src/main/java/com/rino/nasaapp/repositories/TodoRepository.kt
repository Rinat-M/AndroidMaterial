package com.rino.nasaapp.repositories

import com.rino.nasaapp.entities.Todo

interface TodoRepository {

    fun getTodos(): Result<List<Todo>>

    fun removeTodo(position: Int): Todo

    fun removeTodo(todo: Todo)

    fun moveTodo(fromPosition: Int, toPosition: Int)

    fun searchTodo(query: String): Result<List<Todo>>

    fun saveTodo(todo: Todo)

    fun generateTodo(): Todo

}