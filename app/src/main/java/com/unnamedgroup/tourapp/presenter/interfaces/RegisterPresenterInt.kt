package com.unnamedgroup.tourapp.presenter.interfaces

import android.content.Context
import com.unnamedgroup.tourapp.model.business.User
import com.unnamedgroup.tourapp.utils.BaseView

interface RegisterPresenterInt {

    fun registerUser(name: String, email: String, password: String, dni: String)
    fun onRegisterUserOk(user: User)
    fun onRegisterUserFailed(error: String)

    interface View: BaseView {
        fun onRegisterUserOk(user: User)
        fun onRegisterUserFailed(error: String)
        fun getViewContext(): Context
    }

}