package com.unnamedgroup.tourapp.presenter.interfaces

import com.unnamedgroup.tourapp.model.business.User
import com.unnamedgroup.tourapp.utils.BaseView

interface LoginPresenterInt {

    fun getLogin(email: String, password: String)
    fun onGetLoginOk(user: User)
    fun onGetLoginFailed(error: String)

    interface View: BaseView {
        fun onGetLoginOk(user: User)
        fun onGetLoginFailed(error: String)
    }

}