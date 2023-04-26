package com.efan.planact.ui.fragments.login

import android.app.Activity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.efan.planact.R
import com.efan.planact.databinding.FragmentLoginBinding
import com.efan.planact.util.PreferencesManager
import com.efan.planact.util.StateListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel by viewModels<UserViewModel>()

    lateinit var preferencesManager: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.tosca)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.viewModel = viewModel
        viewModel.stateListener = this

        preferencesManager = PreferencesManager(requireContext()) // login and log out setup

        emailFocusListener()
        passwordFocusListener()
        toSignUpFragment()

        return binding.root
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun toSignUpFragment() {
        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun inputCheck(): Boolean {
        val validEmailText = validEmail()
        if (validEmailText != null) {
            binding.emailFieldLayout.error = validEmailText
            return false
        } else {
            binding.emailFieldLayout.helperText = null
            binding.emailFieldLayout.error = null
        }

        val validPasswordText = validPassword()
        if (validPasswordText != null) {
            binding.passwordFieldLayout.error = validPasswordText
            return false
        } else {
            binding.passwordFieldLayout.helperText = null
            binding.passwordFieldLayout.error = null
        }
        return true
    }

    private fun emailFocusListener() {
        binding.emailField.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.emailFieldLayout.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.emailField.text.toString()
        if (emailText.isBlank()) {
            return "Required"
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Invalid Email Address"
        }
        return null
    }

    private fun passwordFocusListener() {
        binding.passwordField.setOnFocusChangeListener { view, focused ->
            if (!focused) {
                binding.passwordFieldLayout.helperText = validPassword()
                hideKeyboard(view)
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.passwordField.text.toString()
        if (passwordText.isBlank()) {
            return "Required"
        }

        if (passwordText.length < 8) {
            return "Minimum 8 character password"
        }

        if (!passwordText.matches(".*[A-Z].*".toRegex())) {
            return "Must contain 1 uppercase character"
        }

        if (!passwordText.matches(".*[a-z].*".toRegex())) {
            return "Must contain 1 lowercase character"
        }

        if (!passwordText.matches(".*[@#\$%^&+=].*".toRegex())) {
            return "Must contain 1 special character (@#\$%^&+=)"
        }
        return null
    }

    override fun onLoading() {
    }

    override fun onSuccess(message: String?) {
    }

    override fun onSuccess(message: String?, id: Int) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

        // set session
        preferencesManager.setLogin(true)
        preferencesManager.setId(id)

        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }

    override fun onError(message: String) {
        inputCheck()
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}