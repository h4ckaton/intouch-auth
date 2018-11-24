/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.security.service.impl

import com.intouch.auth.security.User
import org.springframework.context.annotation.Primary
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
@Primary
class UserDeviceServiceImpl(private val userRepository: com.intouch.auth.repository.UserRepository,
                            private val deviceRepository: com.intouch.auth.repository.DeviceRepository) : UserDetailsService {
    /**
     * It's used only on login with username and password
     * DeviceEntity is created here
     */
    @Transactional
    override fun loadUserByUsername(username: String): User? {
        val userEntity = userRepository.find(username).fold({ return null }, { it })
        val deviceEntity = com.intouch.auth.model.DeviceEntity(userEntity.id)
        deviceRepository.add(deviceEntity)
        return User(userEntity, deviceEntity.id)
    }
}