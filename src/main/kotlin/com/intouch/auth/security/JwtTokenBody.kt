/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.security

class JwtTokenBody(
        val userId: Long = -1,
        val deviceId: Long = -1,
        val exp: Long = -1,
        val roles: Array<String> = arrayOf()
)