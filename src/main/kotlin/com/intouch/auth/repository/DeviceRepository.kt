/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.repository

import arrow.core.Try
import com.intouch.auth.model.DeviceEntity

interface DeviceRepository {
    fun add(deviceEntity: DeviceEntity): Try<Unit>

    fun update(deviceEntity: DeviceEntity): Try<DeviceEntity>

    fun delete(deviceEntity: DeviceEntity): Try<Unit>

    fun delete(userId: Long): Try<Int>

    fun find(deviceId: Long): DeviceEntity
}