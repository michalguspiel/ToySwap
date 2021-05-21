package com.erdees.toyswap

import com.erdees.toyswap.Utils.isEmailValid

class Registration(val password: String, private val confirmPassword: String, val mail: String) {

    private fun passwordTooShort(password: String): Boolean {
        return password.length < 7
    }

    private fun passwordsAreSame(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    private fun passwordContainsDigit(password: String): Boolean {
        return password.any { it.isDigit() }
    }

    private fun passwordIsOnlyDigits(password: String): Boolean {
        return password.all { it.isDigit() }
    }

    fun isLegit(): Boolean {
        return (passwordContainsDigit(password) &&
                !passwordIsOnlyDigits(password) &&
                !passwordTooShort(password)&&
                mail.isEmailValid() &&
                passwordsAreSame(password, confirmPassword))
    }

    val errorMessage: String =
    when {
        !this.mail.isEmailValid() -> "Provide valid email!"
        !passwordsAreSame(password, confirmPassword) -> "Passwords are not same!"
        passwordTooShort(password) -> "Password too short!"
        !passwordContainsDigit(password) -> "Password has to contain at least one digit!"
        passwordIsOnlyDigits(password) -> "Password can't contain digits only!"
        else -> "No error"
    }

}
