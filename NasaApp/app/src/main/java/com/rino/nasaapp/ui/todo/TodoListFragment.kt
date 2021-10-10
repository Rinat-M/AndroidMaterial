package com.rino.nasaapp.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.rino.nasaapp.databinding.ProgressBarAndErrorMsgBinding
import com.rino.nasaapp.databinding.TodoListFragmentBinding
import com.rino.nasaapp.entities.ScreenState
import com.rino.nasaapp.entities.Todo
import org.koin.androidx.viewmodel.ext.android.viewModel

class TodoListFragment : Fragment() {
    companion object {
        fun newInstance() = TodoListFragment()
    }

    private var _binding: TodoListFragmentBinding? = null
    private val binding get() = _binding!!

    private var _includeBinding: ProgressBarAndErrorMsgBinding? = null
    private val includeBinding get() = _includeBinding!!

    private val todoListViewModel: TodoListViewModel by viewModel()

    private val todosAdapter = TodosAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todoListViewModel.fetchData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TodoListFragmentBinding.inflate(layoutInflater, container, false)
        _includeBinding = ProgressBarAndErrorMsgBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.todosRecyclerView.adapter = todosAdapter

        todoListViewModel.state.observe(viewLifecycleOwner) { state ->
            state?.let { processData(state) }
        }
    }

    private fun processData(state: ScreenState<List<Todo>>) {
        when (state) {
            ScreenState.Loading -> {
                binding.todosRecyclerView.isVisible = false
                includeBinding.progressBar.isVisible = true
                includeBinding.errorMsg.isVisible = false
            }

            is ScreenState.Success -> {
                with(binding) {
                    todosRecyclerView.isVisible = true
                    includeBinding.progressBar.isVisible = false
                    includeBinding.errorMsg.isVisible = false

                    todosAdapter.submitList(state.data)
                }
            }

            is ScreenState.Error -> {
                binding.todosRecyclerView.isVisible = false

                with(includeBinding) {
                    progressBar.isVisible = false
                    errorMsg.isVisible = true
                    errorMsg.text = state.error.toString()
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        _includeBinding = null
        super.onDestroyView()
    }
}