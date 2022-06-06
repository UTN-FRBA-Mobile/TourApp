package com.unnamedgroup.tourapp.model.business

import android.os.Parcel
import android.os.Parcelable
import com.unnamedgroup.tourapp.model.rest.UserREST

class User(
    val id: Int,
    val name: String,
    val email: String,
    val dni: String,
    val token: String
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    fun toRest(): UserREST{
        return UserREST(id, name, email, dni, token)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(dni)
        parcel.writeString(token)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}