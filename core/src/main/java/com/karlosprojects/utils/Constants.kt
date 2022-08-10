package com.karlosprojects.utils

import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

object Constants {

    const val BASE_URL = "https://gateway.marvel.com"
    val ts = Timestamp(System.currentTimeMillis()).time.toString()

    const val API_KEY = "66b4cce720f3f567ec171a95c147750e"
    private const val PRIVATE_KEY = "c7491f1a7c9a677bbeca6fdcdc7cedfe582308ac"
    const val limit = "20"

    fun hash(): String {
        val input = "$ts$PRIVATE_KEY$API_KEY"
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray()))
            .toString(16)
            .padStart(32, '0')
    }
}