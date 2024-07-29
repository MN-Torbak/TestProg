package com.example.testprog.usecase

import org.junit.Test

class UseCaseTest {
    @Test
    fun generateRandomNumber() {
        for (i in 0..10) {
            val test = UseCase().generateRandomNumber(5,6)
            println(test)
            assert(test >= 5)
            assert(test <= 10)
        }
    }
}