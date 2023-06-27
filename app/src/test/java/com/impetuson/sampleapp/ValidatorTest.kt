package com.impetuson.sampleapp

import org.junit.Assert.*

import org.junit.Test

class ValidatorTest {

    @Test
    fun emailValidator() {
        val email = "example123@gmail.com"
        val expectedResult = true
        val result = Validator.isEmailValid(email)

        // assertEquals(expected, actual)
        assertEquals(expectedResult, result)
    }

}