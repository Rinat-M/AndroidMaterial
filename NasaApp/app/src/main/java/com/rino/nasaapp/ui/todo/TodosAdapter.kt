package com.rino.nasaapp.ui.todo

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rino.nasaapp.R
import com.rino.nasaapp.databinding.TodoListItemBinding
import com.rino.nasaapp.entities.Priority
import com.rino.nasaapp.entities.Todo
import com.rino.nasaapp.utils.toFormatString
import com.rino.nasaapp.wrappers.ApplyThemeObserver

class TodosAdapter : ListAdapter<Todo, TodosAdapter.TodoViewHolder>(TodoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TodoListItemBinding.inflate(inflater, parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TodoViewHolder(
        private val binding: TodoListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: Todo) {
            with(binding) {
                title.text = todo.title
                createdAt.text = todo.createdAt.toFormatString("yyyy-MM-dd")

                setPriorityColor(priority, todo)
            }

        }

        private fun setPriorityColor(view: View, todo: Todo) {
            val bgColor = if (todo.priority == Priority.HIGH) {
                ContextCompat.getColor(itemView.context, R.color.red_500)
            } else {
                val currentTheme = (view.context as ApplyThemeObserver).getCurrentTheme()

                val typedValue = TypedValue()
                currentTheme?.resolveAttribute(R.attr.listItemBackgroundColor, typedValue, true)

                typedValue.resourceId
            }

            view.setBackgroundColor(bgColor)
        }

    }
}

class TodoDiffCallback : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean = oldItem == newItem
}
