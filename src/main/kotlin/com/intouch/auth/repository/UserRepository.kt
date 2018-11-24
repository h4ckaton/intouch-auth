/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.repository

import arrow.core.Option
import arrow.core.Try
import com.intouch.auth.model.UserEntity

interface UserRepository {
    fun find(name: String): Option<UserEntity>

    fun find(id: Long): Option<UserEntity>

    fun add(user: UserEntity): Try<Unit>

    fun update(user: UserEntity): Try<UserEntity>
}