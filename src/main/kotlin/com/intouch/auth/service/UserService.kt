/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.service

import arrow.core.Option
import com.intouch.auth.model.UserEntity
import com.intouch.lib.auth.dto.RegisterRequest

interface UserService {
    fun resetPassword(mail: String)

    fun register(request: RegisterRequest): Option<UserEntity>
}
