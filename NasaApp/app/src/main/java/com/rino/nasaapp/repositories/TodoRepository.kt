package com.rino.nasaapp.repositories

import com.rino.nasaapp.entities.Todo

interface TodoRepository {

    fun getTodos(): Result<List<Todo>>

}