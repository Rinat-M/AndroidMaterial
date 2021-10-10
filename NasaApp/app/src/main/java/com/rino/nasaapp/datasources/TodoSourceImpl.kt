package com.rino.nasaapp.datasources

import com.rino.nasaapp.entities.Priority
import com.rino.nasaapp.entities.Todo
import java.util.*
import kotlin.collections.ArrayList

class TodoSourceImpl : TodoSource {

    private val todoList: ArrayList<Todo> = arrayListOf(
        Todo(1, "Первая задача", "Первая задача в списке", Date(1633082400000), Priority.NORMAL),
        Todo(2, "Вторая задача", "Вторая задача в списке", Date(1633168800000), Priority.NORMAL),
        Todo(3, "Третья задача", "Третья задача в списке", Date(1633255200000), Priority.HIGH),
        Todo(4, "Четвертая задача", "Четвертая задача в списке", Date(1633341600000), Priority.NORMAL),
        Todo(5, "Пятая задача", "Пятая задача в списке", Date(1633428000000), Priority.HIGH)
    )

    override fun getTodos(): List<Todo> = todoList.toList()

    override fun addTodo(todo: Todo): Int {
        todoList.add(todo)
        return getSize() - 1
    }

    override fun removeTodo(position: Int) = todoList.removeAt(position)

    override fun saveTodo(todo: Todo) {
        todoList.firstOrNull { it.id == todo.id }
    }

    override fun moveTodo(fromPosition: Int, toPosition: Int) {
        todoList.removeAt(fromPosition).apply {
            val newPositionIndex = if (toPosition > fromPosition) toPosition - 1 else toPosition
            todoList.add(newPositionIndex, this)
        }
    }

    override fun searchTodo(query: String): List<Todo> {
        return todoList.filter { todo ->
            todo.title.contains(query, ignoreCase = true) or
                    todo.text.contains(query, ignoreCase = true)
        }
    }

    override fun getSize(): Int = todoList.size

    override fun generateNewId(): Int =
        todoList.maxOfOrNull { it.id }?.let { lastId -> lastId + 1 } ?: 1

}