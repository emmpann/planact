package com.efan.planact.ui.fragments.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.efan.planact.R
import com.efan.planact.databinding.FragmentBottomSheetBinding
import com.efan.planact.util.PreferencesManager
import com.efan.planact.util.SharedPreferences
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBottomSheetBinding

    lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBottomSheetBinding.inflate(layoutInflater, container, false)

        preferencesManager = PreferencesManager(requireContext())

        binding.logoutButton.setOnClickListener {
            preferencesManager.removeData()
            SharedPreferences.saveUserId(0)
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            this.dismiss()
        }

        return binding.root
    }
}