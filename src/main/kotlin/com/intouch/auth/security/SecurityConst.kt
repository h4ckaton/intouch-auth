/*
 * Copyright (c) 2018. This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.security

object SecurityConstants {
    const val REFRESH_TOKEN_EXPIRATION_TIME = 2_592_000_000L // 30 days
    const val RESOURCE_TOKEN_EXPIRATION_TIME = 300_000L // 5 minutes
    const val SECRET_TOKEN_LENGTH = 128
    const val SECRET_TOKEN_DICTIONARY: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    const val TOKEN_PREFIX = "Bearer "
    const val AUTHORIZATION_HEADER = "Authorization"
    const val ACAO = "Access-Control-Allow-Origin"
    const val ACAO_VALUE = "http://localhost:8080"
    const val REFRESH_TOKEN = "REFRESH_TOKEN"
    const val ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN"

}

object JwtTokenCustomFields {
    const val USER_ID = "userId"
    const val DEVICE_ID = "deviceId"
    const val ROLES = "roles"
}

object Roles {
    // User
    const val USER = "ROLE_USER"
}

object Privileges {
    // User
    const val RATE_PRODUCT = "RATE_PRODUCT_PRIVILEGE"
    const val ADD_PRODUCT = "ADD_PRODUCT_PRIVILEGE"
}