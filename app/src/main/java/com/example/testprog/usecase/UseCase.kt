package com.example.testprog.usecase

class UseCase {
    fun generateRandomNumberForCountdown(): Int {
        val generateByRandom = Math.random()*6+5
        return generateByRandom.toInt()
    }

    fun generateRandomNumberBetweenOneAndThree(): Int {
        val generateByRandom = Math.random()*3+1
        return generateByRandom.toInt()
    }



    companion object {
        fun generateRandomNumberForCountdown(): Int {
            val generateByRandom = Math.random()*6+5
            return generateByRandom.toInt()
        }

        fun generateRandomNumberBetweenOneAndThree(): Int {
            val generateByRandom = Math.random()*3+1
            return generateByRandom.toInt()
        }
    }
}