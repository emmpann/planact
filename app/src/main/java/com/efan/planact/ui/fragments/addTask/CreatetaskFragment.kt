package com.efan.planact.ui.fragments.addTask

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.efan.planact.R
import com.efan.planact.databinding.FragmentCreatetaskBinding
import com.efan.planact.util.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatetaskFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentCreatetaskBinding

    private val viewModel by viewModels<CreatetaskViewModel>()

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        (activity as AppCompatActivity).window.statusBarColor =
//            ContextCompat.getColor(requireContext(), R.color.white)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_createtask, container, false)
        binding.viewModel = viewModel

//        take logged in userId
//        viewModel.currentUserId.value = SharedPreferences.getUserId(requireContext())
        viewModel.stateListener = this
//        Log.d("createTask", viewModel.currentUserId.toString())

//        Toast.makeText(requireContext(), viewModel.currentUserId.toString(), Toast.LENGTH_SHORT).show()

        initUi()

        return binding.root
    }

    private fun initUi() {
//        set default string on the button
        initFieldButton()

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

        binding.timeButton.setOnClickListener {

            val isSystem24Hour = is24HourFormat(requireContext())
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

    private fun initFieldButton() {
//        task title
        binding.taskTitleField.text.clear()

//        button
        viewModel.dueDate.value = "Today"
        viewModel.dueTime.value = "Add time"

        binding.addDateButtonText.text = viewModel.dueDate.value
        binding.addTimeButtonText.text = viewModel.dueTime.value
    }

    override fun onLoading() {
    }

    override fun onSuccess(message: String?) {
//        clear the field and get back button init
        initFieldButton()
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//        findNavController().navigate(R.id.action_createtaskFragment_to_homeFragment)
    }

    override fun onSuccess(message: String?, timeInMillis: Long) {
        Log.d("notification time", timeInMillis.toString())
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