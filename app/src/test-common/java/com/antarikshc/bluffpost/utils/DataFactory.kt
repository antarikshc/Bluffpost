package com.antarikshc.bluffpost.utils

import java.util.*
import kotlin.random.Random

object DataFactory {

    fun randomUuid(): String = UUID.randomUUID().toString()

    fun randomString() = randomUuid()

    fun randomInt(from: Int = 0, until: Int = 1000): Int = Random.nextInt(from, until)

    fun randomLong(from: Long = 0, until: Long = 1000): Long =
        randomInt(from.toInt(), until.toInt()).toLong()

    fun randomBoolean(): Boolean = Math.random() < 0.5

}