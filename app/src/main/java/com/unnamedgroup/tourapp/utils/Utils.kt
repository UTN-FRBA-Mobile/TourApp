package com.unnamedgroup.tourapp.utils

import android.content.Intent
import android.graphics.Bitmap
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class Utils {
    companion object {
        fun getDateWithFormat(date: Date, format: String): String {
            val formatter = SimpleDateFormat(format, Locale("es", "AR"))
            return formatter.format(date)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }

        fun parseDateWithFormat(date: String, format: String): Date {
            val formatter = SimpleDateFormat(format, Locale("es", "AR"))
            return formatter.parse(date)!!
        }

        private val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        fun checkEmail(email: String): Boolean {
            return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
        }
    }
}