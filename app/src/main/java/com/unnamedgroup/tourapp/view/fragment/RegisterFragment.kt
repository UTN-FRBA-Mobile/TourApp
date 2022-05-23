package com.unnamedgroup.tourapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.unnamedgroup.tourapp.databinding.FragmentRegisterBinding
import com.unnamedgroup.tourapp.model.business.User
import com.unnamedgroup.tourapp.presenter.implementation.LoginPresenterImpl
import com.unnamedgroup.tourapp.presenter.interfaces.LoginPresenterInt

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RegisterFragment : Fragment(),
    LoginPresenterInt.View {

    private var _binding: FragmentRegisterBinding? = null
    private var loginPresenter : LoginPresenterInt = LoginPresenterImpl(this)

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGetLoginOk(user: User) {
        TODO("Not yet implemented")
    }

    override fun onGetLoginFailed(error: String) {
        TODO("Not yet implemented")
    }
}