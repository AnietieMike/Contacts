package com.decagon.android.sq007.util

import java.util.regex.Pattern

object ContactValidator {

    fun verifyPhoneNum(phoneNo: String): Boolean {
        val checker = Pattern.compile("^(0|234)((70)|([89][01]))[0-9]{8}\$")
        val m = checker.matcher(phoneNo)
        return m.matches()
    }
    fun validateEmail(email: String): Boolean {
        val checker = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
        )
        val mCheck = checker.matcher(email)
        return mCheck.matches()
    }

    fun validateName(name: String): Boolean = name.isEmpty()
}
