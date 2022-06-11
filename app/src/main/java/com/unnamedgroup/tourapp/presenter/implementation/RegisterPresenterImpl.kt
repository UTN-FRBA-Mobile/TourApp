package com.unnamedgroup.tourapp.presenter.implementation

import com.unnamedgroup.tourapp.model.business.User
import com.unnamedgroup.tourapp.presenter.interfaces.RegisterPresenterInt
import com.unnamedgroup.tourapp.repository.Repository
import com.unnamedgroup.tourapp.utils.AbstractPresenter
import com.unnamedgroup.tourapp.utils.MyPreferences

class RegisterPresenterImpl(private val mView: RegisterPresenterInt.View) : RegisterPresenterInt,
    AbstractPresenter<RegisterPresenterInt.View>(
        mView
    ) {
    override fun registerUser(name: String, email: String, password: String, dni: String) {
        Repository().registerUser(this, name, email, password, dni)
    }

    override fun onRegisterUserOk(user: User) {
        MyPreferences.saveUser(mView.getViewContext(), user)
        mView.onRegisterUserOk(user)
    }

    override fun onRegisterUserFailed(error: String) {
        mView.onRegisterUserFailed(error)
    }

}