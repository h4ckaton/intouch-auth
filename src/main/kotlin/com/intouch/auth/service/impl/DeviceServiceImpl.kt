/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.service.impl

import com.intouch.auth.model.DeviceEntity
import com.intouch.auth.model.UserEntity
import com.intouch.auth.repository.DeviceRepository
import com.intouch.auth.security.generateSecret
import com.intouch.auth.service.DeviceService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeviceServiceImpl(private val deviceRepository: DeviceRepository) : DeviceService {
    @Transactional
    override fun generateSecrets(deviceEntity: DeviceEntity): Pair<String, String> =
            deviceEntity.run {
                refreshSecret = generateSecret()
                resourceSecret = generateSecret()
                deviceRepository.update(this)
            }.fold({ throw it }, { it }).run {
                return Pair(resourceSecret, refreshSecret)
            }

    @Transactional
    override fun logoutAllDevices(userEntity: UserEntity): Boolean {
        SecurityContextHolder.getContext().authentication = null
        return deviceRepository.delete(userEntity.id).isSuccess()
    }

    @Transactional
    override fun logout(deviceEntity: DeviceEntity): Boolean {
        SecurityContextHolder.getContext().authentication = null
        return deviceRepository.delete(deviceEntity).isSuccess()
    }
}