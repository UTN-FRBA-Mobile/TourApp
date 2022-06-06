package com.unnamedgroup.tourapp.utils

import android.content.Intent
import android.graphics.Bitmap
import java.text.SimpleDateFormat
import java.util.*

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
    }
}