package com.rino.nasaapp.datasources

import com.rino.nasaapp.entities.Todo

interface TodoSource {

    fun getTodos(): List<Todo>

    fun addTodo(todo: Todo): Int

    fun removeTodo(id: Int): Boolean

    fun removeTodo(todo: Todo): Boolean

    fun saveTodo(todo: Todo)

    fun getSize(): Int

    fun generateNewId(): Int
}