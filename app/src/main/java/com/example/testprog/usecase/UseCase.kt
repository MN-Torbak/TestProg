package com.example.testprog.usecase

class UseCase {
    fun generateRandomNumber(startingNumber: Int, size: Int): Int {
        val generateByRandom = Math.random()*size+startingNumber
        return generateByRandom.toInt()
    }
}