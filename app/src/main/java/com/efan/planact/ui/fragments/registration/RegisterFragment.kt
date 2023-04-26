package com.efan.planact.ui.fragments.registration

import android.os.Bundle
import android.util.Patterns
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
import com.efan.planact.R
import com.efan.planact.databinding.FragmentRegisterBinding
import com.efan.planact.ui.fragments.login.UserViewModel
import com.efan.planact.util.StateListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.view.*

@AndroidEntryPoint
class RegisterFragment : Fragment(), StateListener {

    private lateinit var binding: FragmentRegisterBinding

    private val viewModel by viewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.tosca_white1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.viewModel = viewModel
        viewModel.stateListener = this
        val view = binding.root

        emailFocusListener()
        usernameFocusListener()
        passwordFocusListener()
        confirmPasswordFocusListener()
        toSignInFragment()

        return view
    }

    private fun toSignInFragment() {
        binding.signinButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
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

    override fun onLoading() {
    }

    override fun onSuccess(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    override fun onSuccess(message: String?, id: Int) {
    }

    override fun onError(message: String) {
        inputCheck()
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}