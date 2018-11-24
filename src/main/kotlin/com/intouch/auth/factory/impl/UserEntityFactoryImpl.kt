/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.factory.impl

import arrow.core.getOrElse
import com.intouch.auth.factory.UserEntityFactory
import com.intouch.auth.repository.RoleRepository
import com.intouch.lib.auth.Roles
import com.intouch.lib.auth.dto.RegisterRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserEntityFactoryImpl(private val roleRepository: RoleRepository,
                            private val passwordEncoder: PasswordEncoder) : UserEntityFactory {
    override fun create(username: String, email: String, password: String) = runBlocking {
        com.intouch.auth.model.UserEntity(username, email).apply {
            launch { passwordHash = passwordEncoder.encode(password) }
            launch {
                val role = roleRepository.find(Roles.USER).getOrElse { throw RuntimeException("Database default data not inserted.") }
                roles.add(role)
            }
        }
    }

    override fun create(registerRequest: RegisterRequest) = registerRequest.run { create(name, email, password) }
}