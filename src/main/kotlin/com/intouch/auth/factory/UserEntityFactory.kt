/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.factory

import com.intouch.auth.model.UserEntity
import com.intouch.lib.auth.dto.RegisterRequest

interface UserEntityFactory {
    fun create(username: String, email: String, password: String): UserEntity

    fun create(registerRequest: RegisterRequest): UserEntity
}