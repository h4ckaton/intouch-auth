/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.security.service.impl

import com.intouch.auth.security.SecurityConstants.REFRESH_TOKEN_EXPIRATION_TIME
import com.intouch.auth.security.SecurityConstants.RESOURCE_TOKEN_EXPIRATION_TIME
import com.intouch.auth.security.SecurityConstants.ROLE_REFRESH_TOKEN
import com.intouch.auth.security.User
import com.intouch.auth.security.generateToken
import com.intouch.auth.security.service.JwtTokenService
import com.intouch.auth.service.DeviceService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class JwtTokenServiceImpl(private val deviceService: DeviceService) : JwtTokenService {
    override fun generateTokens(authentication: Authentication): Pair<String, String> {
        val user = authentication.principal as User
        return user.deviceEntity.run {
            val secrets = deviceService.generateSecrets(this)

            val resourceToken = generateToken(user.userEntity.id, id,
                    RESOURCE_TOKEN_EXPIRATION_TIME,
                    user.authorities.map { it.authority }.toTypedArray(),
                    secrets.first)
            val refreshToken = generateToken(user.userEntity.id, id, REFRESH_TOKEN_EXPIRATION_TIME, arrayOf(ROLE_REFRESH_TOKEN), secrets.second)
            Pair(resourceToken, refreshToken)
        }
    }
}