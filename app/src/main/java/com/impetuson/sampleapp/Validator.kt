package com.impetuson.sampleapp

// Created for sample Unit Testing
object Validator {
    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return email.matches(emailRegex)
    }
}