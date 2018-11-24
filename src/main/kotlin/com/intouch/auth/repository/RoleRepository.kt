/*
 * Copyright (c) 2018. This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.repository


import arrow.core.Option
import com.intouch.auth.model.RoleEntity

interface RoleRepository {
    fun find(username: String): Option<RoleEntity>
}