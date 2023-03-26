package com.efan.planact.ui.fragments.registration

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.efan.planact.R
import com.efan.planact.data.user.User
import com.efan.planact.data.user.UserViewModel
import com.efan.planact.databinding.FragmentRegisterBinding
import kotlinx.android.synthetic.main.fragment_register.view.*

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        toLoginFragment(view)
        emailFocusListener()
        usernameFocusListener()
        passwordFocusListener()
        confirmPasswordFocusListener()
        registerButton()

        return view
    }

    private fun registerButton() {
        binding.registerButton.setOnClickListener {
            if (inputCheck()) {
                insertDataToDatabase()
                Toast.makeText(requireContext(), "Successfully register", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun insertDataToDatabase() {
        val email = binding.emailField.text.toString()
        val username = binding.usernameField.text.toString()
        val password = binding.passwordField.text.toString()

        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val user = User(0, email, username, password)

        mUserViewModel.addUser(user)

    }

    private fun inputCheck(): Boolean {

        if (validEmail()?.isNotEmpty() == true) {
            binding.emailFieldLayout.error = validEmail()
            return false
        } else {
            binding.emailFieldLayout.helperText = validEmail()
            binding.emailFieldLayout.error = validEmail()
        }

        if (validUsername()?.isNotEmpty() == true) {
            binding.usernameFieldLayout.error = validUsername()
            return false
        } else {
            binding.usernameFieldLayout.helperText = validUsername()
            binding.usernameFieldLayout.error = validUsername()
        }

        if (validPassword()?.isNotEmpty() == true) {
            binding.passwordFieldLayout.error = validPassword()
            return false
        } else {
            binding.passwordFieldLayout.helperText = validPassword()
            binding.passwordFieldLayout.error = validPassword()
        }

        if (validPassword()?.isNotEmpty() == true) {
            binding.confirmPasswordFieldLayout.error = validPassword()
            return false
        } else {
            binding.confirmPasswordFieldLayout.helperText = validPassword()
            binding.confirmPasswordFieldLayout.error = validPassword()
        }
        return true
    }

    private fun toLoginFragment(view: View) {
        view.signin_button.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
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

    private fun usernameFocusListener() {
        binding.usernameField.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.usernameFieldLayout.helperText = validUsername()
            }
        }
    }

    private fun validUsername(): String? {
        val usernameText = binding.usernameField.text.toString()
        if (usernameText.isBlank()) {
            return "Required"
        }
        return null
    }

    private fun passwordFocusListener() {
        binding.passwordField.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.passwordFieldLayout.helperText = validPassword()
                binding.confirmPasswordFieldLayout.helperText = validPassword()
            }
        }
    }

    private fun confirmPasswordFocusListener() {
        binding.confirmPasswordField.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.confirmPasswordFieldLayout.helperText = validPassword()
                binding.passwordFieldLayout.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.passwordField.text.toString()
        val confirmPasswordText = binding.confirmPasswordField.text.toString()

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

        if (passwordText != confirmPasswordText) {
            return "Password doesn't match"
        }
        return null
    }
}