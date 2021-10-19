package com.rino.nasaapp.ui.todo

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rino.nasaapp.databinding.TodoDialogFragmentBinding
import com.rino.nasaapp.entities.Priority
import com.rino.nasaapp.entities.Todo
import com.rino.nasaapp.utils.toFormatString
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.util.*

class TodoBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(): TodoBottomSheetDialogFragment = TodoBottomSheetDialogFragment()
    }

    private var _binding: TodoDialogFragmentBinding? = null
    private val binding get() = _binding!!

    private val todoListViewModel: TodoListViewModel by lazy {
        requireParentFragment().getViewModel()
    }

    private var currentTodo: Todo? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = TodoDialogFragmentBinding.inflate(layoutInflater)

        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(binding.root)

        currentTodo = todoListViewModel.selectedTodo

        updateTodoInfo()

        return dialog
    }

    private fun updateTodoInfo() {
        currentTodo?.let { todo ->
            with(binding) {
                todoTitleEditText.setText(todo.title)
                todoTextEditText.setText(todo.text)
                todoPriorityCheckBox.isChecked = todo.priority == Priority.HIGH
                todoCreatedAtTextView.text = todo.createdAt.toFormatString("yyyy-MM-dd")
            }

            initDatePicker()
        }
    }

    private fun initDatePicker() {
        val calendar = Calendar.getInstance()

        binding.todoCreatedAtTextView.setOnClickListener {
            val dateSetListener =
                OnDateSetListener { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    calendar.set(
                        year,
                        monthOfYear,
                        dayOfMonth
                    )
                    currentTodo = currentTodo?.copy(createdAt = Date(calendar.timeInMillis))
                    binding.todoCreatedAtTextView.text =
                        currentTodo?.createdAt?.toFormatString("yyyy-MM-dd")
                }

            calendar.time = Date()

            val dialog = DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            dialog.show()
        }
    }

    override fun onStop() {
        super.onStop()
        currentTodo = currentTodo?.copy(
            title = binding.todoTitleEditText.text.toString(),
            text = binding.todoTextEditText.text.toString(),
            priority = if (binding.todoPriorityCheckBox.isChecked) Priority.HIGH else Priority.NORMAL
        )

        currentTodo?.let { todoListViewModel.saveTodo(it) }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}