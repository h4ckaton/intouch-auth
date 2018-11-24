/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.service

import com.intouch.auth.model.DeviceEntity
import com.intouch.auth.model.UserEntity

interface DeviceService {
    /**
     * <resourceSecret, refreshSecret>
     */
    fun generateSecrets(deviceEntity: DeviceEntity): Pair<String, String>

    fun logout(deviceEntity: DeviceEntity): Boolean

    fun logoutAllDevices(userEntity: UserEntity): Boolean
}