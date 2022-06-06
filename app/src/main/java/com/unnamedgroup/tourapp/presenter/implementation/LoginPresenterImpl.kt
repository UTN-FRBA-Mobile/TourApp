package com.unnamedgroup.tourapp.presenter.implementation

import com.unnamedgroup.tourapp.model.business.User
import com.unnamedgroup.tourapp.presenter.interfaces.LoginPresenterInt
import com.unnamedgroup.tourapp.repository.Repository
import com.unnamedgroup.tourapp.utils.AbstractPresenter
import com.unnamedgroup.tourapp.utils.MyPreferences

class LoginPresenterImpl(private val mView: LoginPresenterInt.View) : LoginPresenterInt,
    AbstractPresenter<LoginPresenterInt.View>(
        mView
    ) {
    override fun getLogin(email: String, password: String) {
        Repository().getLogin(this, email, password)
    }

    override fun onGetLoginOk(user: User) {
        MyPreferences.saveUser(mView.getViewContext(), user)
        mView.onGetLoginOk(user)
    }

    override fun onGetLoginFailed(error: String) {
        mView.onGetLoginFailed(error)
    }

}