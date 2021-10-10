package com.rino.nasaapp.ui.todo

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
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

class TodosAdapter(
    private val itemChangeListener: OnItemChangeListener,
    private val dragListener: OnStartDragListener
) : ListAdapter<Todo, TodosAdapter.TodoViewHolder>(TodoDiffCallback()),
    ItemTouchHelperAdapter {

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
    ) : RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(todo: Todo) {
            with(binding) {
                title.text = todo.title
                createdAt.text = todo.createdAt.toFormatString("yyyy-MM-dd")

                setPriorityColor(priority, todo)

                binding.dragIndicator.setOnTouchListener { _, motionEvent ->
                    if (motionEvent.actionMasked == MotionEvent.ACTION_DOWN) {
                        dragListener.onStartDrag(this@TodoViewHolder)
                    }
                    false
                }
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

        override fun onItemSelected() {
            binding.root.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            binding.root.setBackgroundColor(0)
        }

    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        itemChangeListener.onItemMove(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        itemChangeListener.onItemRemove(position)
    }

    interface OnItemChangeListener {
        fun onItemRemove(position: Int)

        fun onItemMove(fromPosition: Int, toPosition: Int)
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: TodoViewHolder)
    }
}

private class TodoDiffCallback : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean = oldItem == newItem
}

private interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)
}

interface ItemTouchHelperViewHolder {
    fun onItemSelected()

    fun onItemClear()
}