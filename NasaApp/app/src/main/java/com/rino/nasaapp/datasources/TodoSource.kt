package com.rino.nasaapp.datasources

import com.rino.nasaapp.entities.Todo

interface TodoSource {

    fun getTodos(): List<Todo>

    fun addTodo(todo: Todo): Boolean

    fun removeTodo(position: Int): Todo

    fun saveTodo(todo: Todo)

    fun moveTodo(fromPosition: Int, toPosition: Int)

    fun searchTodo(query: String): List<Todo>

    fun getSize(): Int

    fun generateNewId(): Int
}