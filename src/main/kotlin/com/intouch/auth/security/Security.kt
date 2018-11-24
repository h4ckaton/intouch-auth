/*
 * Copyright (c) 2018. This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.intouch.auth.security.SecurityConstants.SECRET_TOKEN_DICTIONARY
import com.intouch.auth.security.SecurityConstants.SECRET_TOKEN_LENGTH
import com.intouch.microbase.security.JwtTokenBody
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.security.SecureRandom
import java.util.*
import javax.crypto.spec.SecretKeySpec

object SecurityConstants {
    const val REFRESH_TOKEN_EXPIRATION_TIME = 2_592_000_000L // 30 days
    const val RESOURCE_TOKEN_EXPIRATION_TIME = 600_00L // 5 minutes
    const val SECRET_TOKEN_LENGTH = 128
    const val SECRET_TOKEN_DICTIONARY: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    const val ACAO = "Access-Control-Allow-Origin"
    const val ACAO_VALUE = "http://localhost:8080"
}

fun generateToken(userId: Long, deviceId: Long, expiration: Long, authorities: Array<String>, secret: String): String =
        JwtTokenBody(userId, deviceId, Date(System.currentTimeMillis() + expiration).time / 1000, authorities).run {
            Jwts.builder().apply {
                setPayload(jacksonObjectMapper().writeValueAsString(this@run))
                signWith(SecretKeySpec(secret.toByteArray(), SignatureAlgorithm.HS512.jcaName))
            }.compact()
        }

fun generateSecret(): String {
    val secureRandom = SecureRandom()
    return StringBuilder().apply {
        repeat(SECRET_TOKEN_LENGTH) {
            append(SECRET_TOKEN_DICTIONARY.run { this[secureRandom.nextInt(length)] })
        }
    }.toString()
}