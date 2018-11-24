/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.security.service

import org.springframework.security.core.Authentication

interface JwtTokenService {
    /**
     * <Resource, Refresh>
     */
    fun generateTokens(authentication: Authentication): Pair<String, String>
}