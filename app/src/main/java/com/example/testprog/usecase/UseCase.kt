package com.example.testprog.usecase

class UseCase {
    fun generateRandomNumber(): Int {
        val generateByRandom = Math.random()*6+5
        return generateByRandom.toInt()
    }
}