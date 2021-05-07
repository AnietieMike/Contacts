package com.decagon.android.sq007.util

import java.util.regex.Pattern

object ContactValidator {

    fun validatePhoneNum(phoneNo: String): Boolean {
        val checker = Pattern.compile("^(0|234)((70)|([89][01]))[0-9]{8}\$")
        val m = checker.matcher(phoneNo)
        return m.matches()
    }

    fun validateName(name: String): Boolean {
        return name.trim().isNotEmpty()
    }
}
