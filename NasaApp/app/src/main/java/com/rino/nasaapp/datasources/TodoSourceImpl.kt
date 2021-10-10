package com.rino.nasaapp.datasources

import com.rino.nasaapp.entities.Priority
import com.rino.nasaapp.entities.Todo
import java.util.*
import kotlin.collections.ArrayList

class TodoSourceImpl : TodoSource {

    private val todoList: ArrayList<Todo> = arrayListOf(
        Todo(1, "Первая задача", "Первая задача в списке", Date(), Priority.NORMAL),
        Todo(2, "Вторая задача", "Вторая задача в списке", Date(), Priority.NORMAL),
        Todo(3, "Третья задача", "Третья задача в списке", Date(), Priority.HIGH)
    )

    override fun getTodos(): List<Todo> = todoList.toList()

    override fun addTodo(todo: Todo): Int {
        todoList.add(todo)
        return getSize() - 1
    }

    override fun removeTodo(id: Int): Boolean =
        todoList.firstOrNull { it.id == id }?.let { todo ->
            todoList.remove(todo)
        } ?: false

    override fun removeTodo(todo: Todo): Boolean = todoList.remove(todo)

    override fun saveTodo(todo: Todo) {
        todoList.firstOrNull { it.id == todo.id }
    }

    override fun getSize(): Int = todoList.size

    override fun generateNewId(): Int =
        todoList.maxOfOrNull { it.id }?.let { lastId -> lastId + 1 } ?: 1

}