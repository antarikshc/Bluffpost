package com.antarikshc.bluffpost.utils

import java.util.*
import kotlin.random.Random

object DataFactory {

    fun randomUuid(): String = UUID.randomUUID().toString()

    fun randomString() = randomUuid()

    fun randomInt(): Int = Random.nextInt(0, 1000 + 1)

    fun randomLong(): Long = randomInt().toLong()

    fun randomBoolean(): Boolean = Math.random() < 0.5

}