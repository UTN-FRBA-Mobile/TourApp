package com.unnamedgroup.tourapp.view.fragment

import android.content.Context
import android.content.Intent
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
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.databinding.FragmentRegisterBinding
import com.unnamedgroup.tourapp.model.business.User
import com.unnamedgroup.tourapp.presenter.implementation.LoginPresenterImpl
import com.unnamedgroup.tourapp.presenter.implementation.RegisterPresenterImpl
import com.unnamedgroup.tourapp.presenter.interfaces.LoginPresenterInt
import com.unnamedgroup.tourapp.presenter.interfaces.RegisterPresenterInt
import com.unnamedgroup.tourapp.utils.Utils
import com.unnamedgroup.tourapp.view.activity.MainActivity

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RegisterFragment : Fragment(),
    RegisterPresenterInt.View {

    private var _binding: FragmentRegisterBinding? = null
    private var registerPresenter: RegisterPresenterInt = RegisterPresenterImpl(this)
    private var registerButton: Button? = null
    private var cancelButton: Button? = null
    private var nameInput: EditText? = null
    private var dniInput: EditText? = null
    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null
    private var repeatPasswordInput: EditText? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerButton = binding.registerButton
        cancelButton = binding.cancelRegisterButton
        nameInput = binding.nameInput
        dniInput = binding.dniInput
        emailInput = binding.emailInput
        passwordInput = binding.passwordInput
        repeatPasswordInput = binding.repeatPasswordInput

        registerButton!!.setOnClickListener {
            checkFields()
        }

        cancelButton!!.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun checkFields() {
        if (
            nameInput!!.text.toString().isEmpty()
            || dniInput!!.text.toString().isEmpty()
            || emailInput!!.text.toString().isEmpty()
            || passwordInput!!.text.toString().isEmpty()
            || repeatPasswordInput!!.text.toString().isEmpty()
        ) {
            Toast.makeText(
                context,
                getString(R.string.must_complete_all_fields),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (passwordInput!!.text.toString() != repeatPasswordInput!!.text.toString()) {
            Toast.makeText(
                context,
                getString(R.string.passwords_not_match),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (passwordInput!!.text.toString().length < 8) {
            Toast.makeText(
                context,
                getString(R.string.passwords_too_short),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (!Utils.checkEmail(emailInput!!.text.toString())) {
            Toast.makeText(
                context,
                getString(R.string.invalid_email),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        registerPresenter.registerUser(
            nameInput!!.text.toString(),
            emailInput!!.text.toString(),
            passwordInput!!.text.toString(),
            dniInput!!.text.toString()
                .replace(".", "")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRegisterUserOk(user: User) {
        goToMainActivity()
    }

    override fun onRegisterUserFailed(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun getViewContext(): Context {
        return requireContext()
    }

    private fun goToMainActivity() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}