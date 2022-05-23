package com.unnamedgroup.tourapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentLoginBinding
import com.unnamedgroup.tourapp.model.business.User
import com.unnamedgroup.tourapp.presenter.implementation.LoginPresenterImpl
import com.unnamedgroup.tourapp.presenter.interfaces.LoginPresenterInt

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment(),
    LoginPresenterInt.View {

    private var _binding: FragmentLoginBinding? = null
    private var loginPresenter: LoginPresenterInt = LoginPresenterImpl(this)
    private var loginButton: Button? = null
    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null
    private var registerButton: TextView? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton = binding.loginButton
        registerButton = binding.registerTxt
        emailInput = binding.emailInput
        passwordInput = binding.passwordInput
        loginButton!!.setOnClickListener {
           checkFields()
        }
        registerButton!!.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun checkFields() {
        if (emailInput!!.text.toString().isEmpty() || passwordInput!!.text.toString().isEmpty()) {
            Toast.makeText(
                context,
                getString(R.string.must_complete_all_fields),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            loginPresenter.getLogin(emailInput!!.text.toString(), passwordInput!!.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGetLoginOk(user: User) {
        TODO("Not yet implemented")
    }

    override fun onGetLoginFailed(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}