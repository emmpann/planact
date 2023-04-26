package com.efan.planact.ui.fragments.editTask

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.efan.planact.R
import com.efan.planact.databinding.FragmentEditTaskBinding
import com.efan.planact.util.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTaskFragment : Fragment(), StateListener {

    private lateinit var binding : FragmentEditTaskBinding

    private val viewModel by viewModels<EditTaskViewModel>()

    private val args by navArgs<EditTaskFragmentArgs>()

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        (activity as AppCompatActivity).window.statusBarColor =
//            ContextCompat.getColor(requireContext(), R.color.white)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_task, container, false)
        binding.viewModel = viewModel

        viewModel.task = args.selectedTask

        viewModel.stateListener = this

        initUI()

        return binding.root
    }

    private fun initUI() {
        // set data to view model variable
        viewModel.taskName.value = args.selectedTask.name
        viewModel.dueDate.value = args.selectedTask.dueDate
        viewModel.dueTime.value = args.selectedTask.dueTime

        // extended add date button
        binding.addDateButtonText.setBackgroundResource(R.drawable.extended_add_date_button)

        // init ui (set task details)
        binding.taskTitleField.setText(viewModel.taskName.value) // task name
        binding.addDateButtonText.text = viewModel.dueDate.value // task date
        binding.addTimeButtonText.text = viewModel.dueTime.value // task time

        // set date button
        binding.dateButton.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            datePicker.show(childFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener {
                viewModel.dueDate.value = datePicker.headerText
                binding.addDateButtonText.text = viewModel.dueDate.value

                // expand button
                binding.addDateButtonText.setBackgroundResource(R.drawable.extended_add_date_button)
            }
        }

        // set time button
        binding.timeButton.setOnClickListener {

            val isSystem24Hour = DateFormat.is24HourFormat(requireContext())
            val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select time")
                .build()
            timePicker.show(childFragmentManager, "TAG")
            timePicker.addOnPositiveButtonClickListener {
                val h = timePicker.hour
                val m = timePicker.minute
                viewModel.dueTime.value = String.format("%02d:%02d", h, m)
                binding.addTimeButtonText.text = viewModel.dueTime.value
            }
        }
    }

    override fun onLoading() {
    }

    override fun onSuccess(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_editTask_to_homeFragment)
    }

    override fun onSuccess(message: String?, timeInMillis: Long) {
        val intent = Intent(requireContext(), Notification::class.java)
        intent.putExtra(titleExtra, "It's time")
        intent.putExtra(messageExtra, "to: $message")

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }

    override fun onError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}