package com.efan.planact.ui.fragments.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.efan.planact.R
import com.efan.planact.databinding.FragmentLoginBinding
import com.efan.planact.ui.activity.MainActivity

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        emailFocusListener()
        passwordFocusListener()
        toSignUpFragment()
        loginButton()

        return view
    }

    private fun toSignUpFragment() {
        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun loginButton() {
        binding.loginButton.setOnClickListener {
            if (inputCheck()) {
                if (isUserExist()) {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
        }
    }

    private fun isUserExist(): Boolean {
        return true
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
        binding.passwordField.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.passwordFieldLayout.helperText = validPassword()
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

//    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
//        val inflater = super.onGetLayoutInflater(savedInstanceState)
//        val contextThemeWrapper: Context = ContextThemeWrapper(requireContext(), R.style.LoginTheme)
//        return inflater.cloneInContext(contextThemeWrapper)
//    }
}