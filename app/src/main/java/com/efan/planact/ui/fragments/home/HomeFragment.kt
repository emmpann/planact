package com.efan.planact.ui.fragments.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.efan.planact.R
import com.efan.planact.data.task.Task
import com.efan.planact.databinding.FragmentHomeBinding
import com.efan.planact.ui.fragments.bottomSheet.BottomSheetFragment
import com.efan.planact.util.SpacingItemDecorator
import com.efan.planact.util.StateListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), TasksAdapter.OnItemClickListener, StateListener {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var bottomSheetFragment: BottomSheetFragment

    private var isBackPressedOnce: Boolean = false

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                if (isBackPressedOnce) {
//                    return
//                }
//
//                Toast.makeText(requireContext(), "Press again to exit", Toast.LENGTH_SHORT)
//                isBackPressedOnce = true
//
//                Handler().postDelayed({
//                    isBackPressedOnce = false
//                }, 2000)
//            }
//        }
//    }

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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.viewModel = viewModel

        val taskAdapter = TasksAdapter(this)

        val spacingItemDecorator = SpacingItemDecorator(35)

        binding.apply {
            recyclerViewTasks.apply {
                addItemDecoration(spacingItemDecorator)
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = taskAdapter.currentList[viewHolder.adapterPosition]
                    viewModel!!.onTaskSwiped(task)
                }
            }).attachToRecyclerView(recyclerViewTasks)
        }

        // get all tasks
        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

        // get searched tasks
        viewModel.tasksSearchFlow.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

        // search logic
        binding.searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.searchQuery.value = binding.searchField.text.toString()
            }

        })

        // dashboard button
        binding.dashboardButton.setOnClickListener {
            bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(parentFragmentManager, tag)
        }

        return binding.root
    }

    override fun onItemClick(task: Task) {
        // home page to edit task page (passing data)
        val action = HomeFragmentDirections.actionHomeFragmentToEditTask(task)
        findNavController().navigate(action)
    }

    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task, isChecked)
    }

    override fun onLoading() {}

    override fun onSuccess(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(message: String?, id: Int) {}

    override fun onError(message: String) {}
}