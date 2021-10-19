package com.rino.nasaapp.entities

import java.util.*

data class Todo(
    val id: Int,
    val title: String,
    val text: String,
    val createdAt: Date,
    val priority: Priority
)