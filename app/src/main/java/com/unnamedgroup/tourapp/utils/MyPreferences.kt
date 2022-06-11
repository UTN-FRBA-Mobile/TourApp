package com.unnamedgroup.tourapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.unnamedgroup.tourapp.model.business.User

object MyPreferences {
    private const val userLoggedInId = "user_id"
    private const val userLoggedInEmail = "user_email"
    private const val userLoggedInName = "user_name"
    private const val userLoggedInDNI = "user_dni"
    private const val userLoggedInToken = "user_token"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }

    fun getUserId(context: Context): Int {
        return getPreferences(context).getInt(userLoggedInId, -1)
    }

    fun getUserName(context: Context): String? {
        return getPreferences(context).getString(userLoggedInName, "")
    }

    fun getUserEmail(context: Context): String? {
        return getPreferences(context).getString(userLoggedInEmail, "")
    }

    fun getUserDNI(context: Context): String? {
        return getPreferences(context).getString(userLoggedInDNI, "")
    }

    fun getUserToken(context: Context): String? {
        return getPreferences(context).getString(userLoggedInToken, "")
    }

    fun saveUser(context: Context, user: User) {
        val sp: SharedPreferences = getPreferences(context)
        val spEditor = sp.edit()
        spEditor.putInt(userLoggedInId, user.id)
        spEditor.putString(userLoggedInName, user.name)
        spEditor.putString(userLoggedInEmail, user.email)
        spEditor.putString(userLoggedInDNI, user.dni)
        spEditor.putString(userLoggedInToken, user.token)
        spEditor.apply()
    }

    fun logout(context: Context) {
        val sp: SharedPreferences = getPreferences(context)
        val spEditor = sp.edit()
        spEditor.putInt(userLoggedInId, -1)
        spEditor.putString(userLoggedInName, "")
        spEditor.putString(userLoggedInEmail, "")
        spEditor.putString(userLoggedInDNI, "")
        spEditor.putString(userLoggedInToken, "")
        spEditor.apply()
    }

}