package com.example.daylik

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.daylik.adapters.TaskAdapter
import com.example.daylik.database.AppDatabase
import com.example.daylik.database.Task
import com.example.daylik.databinding.FragmentSecondBinding // Исправлено на FragmentSecondBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null // Исправлено на FragmentSecondBinding
    private val binding get() = _binding!!
    private lateinit var taskAdapter: TaskAdapter
    private val database by lazy { AppDatabase.getDatabase(requireContext()) }
    private val taskDao by lazy { database.taskDao() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false) // Исправлено на FragmentSecondBinding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
        observeTasks()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter { updatedTask ->
            lifecycleScope.launch {
                taskDao.update(updatedTask)
            }
        }
        binding.recyclerView.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupClickListeners() {
        binding.btnAddTask.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun observeTasks() {
        lifecycleScope.launch {
            taskDao.getPermanentTasks().collect { tasks ->
                taskAdapter.submitList(tasks)
            }
        }
    }

    private fun showAddTaskDialog() {
        // Используем кастомный макет диалога
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task, null)
        val input = dialogView.findViewById<TextInputEditText>(R.id.etTaskTitle)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Новая задача")
            .setView(dialogView) // Установка кастомного макета
            .setPositiveButton("Добавить") { dialog, _ ->
                input.text?.toString()?.let { title ->
                    val task = Task(
                        title = title,
                        isCompleted = false,
                        isTemporary = false // Для постоянных задач
                    )
                    lifecycleScope.launch {
                        taskDao.insert(task)
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}