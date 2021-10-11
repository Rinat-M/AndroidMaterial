package com.rino.nasaapp.repositories

import com.rino.nasaapp.datasources.TodoSource
import com.rino.nasaapp.entities.Todo

class TodoRepositoryImpl(
    private val todoSource: TodoSource
) : TodoRepository {

    override fun getTodos(): Result<List<Todo>> = Result.success(todoSource.getTodos())

    override fun removeTodo(position: Int): Todo = todoSource.removeTodo(position)

    override fun removeTodo(todo: Todo) = todoSource.removeTodo(todo)

    override fun moveTodo(fromPosition: Int, toPosition: Int) =
        todoSource.moveTodo(fromPosition, toPosition)

    override fun searchTodo(query: String): Result<List<Todo>> =
        Result.success(todoSource.searchTodo(query))

    override fun saveTodo(todo: Todo) = todoSource.saveTodo(todo)


}