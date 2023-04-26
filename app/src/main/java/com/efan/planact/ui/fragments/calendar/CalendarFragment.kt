package com.efan.planact.ui.fragments.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.efan.planact.R
import com.efan.planact.data.task.Task
import com.efan.planact.databinding.FragmentCalendarBinding
import com.efan.planact.ui.fragments.home.TasksAdapter
import com.efan.planact.util.DateUtil
import com.efan.planact.util.SpacingItemDecorator
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarFragment : Fragment(), TasksAdapter.OnItemClickListener {

    private lateinit var binding : FragmentCalendarBinding

    private val viewModel by viewModels<CalendarViewModel>()

    private lateinit var bottomSheetFragment: BottomSheetDialogFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.tosca_light)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        binding.viewModel = viewModel

        val taskAdapter = TasksAdapter(this)

        val spacingItemDecorator = SpacingItemDecorator(35)

        binding.apply {
            calendarRecyclerViewTasks.apply {
                addItemDecoration(spacingItemDecorator)
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        // init recycler content (tasks)
        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

//        init
        viewModel.dueDate.value = DateUtil.getCurrentDate()

        binding.datePicker.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = DateUtil.formattedDate(year, month, dayOfMonth)

            Log.d("date", date)

            viewModel.dueDate.value = date
        }

        // dashboard button
//        binding.dashboardButton.setOnClickListener {
//            bottomSheetFragment = BottomSheetFragment()
//            bottomSheetFragment.show(parentFragmentManager, tag)
//        }

        return binding.root
    }

    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task, isChecked)
    }
}