/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.repository.impl

import arrow.core.Option
import arrow.core.Try
import arrow.core.toOption
import com.intouch.auth.findBy
import com.intouch.auth.model.DeviceEntity
import com.intouch.auth.remove
import com.intouch.auth.repository.AbstractRepository
import com.intouch.auth.repository.DeviceRepository
import org.springframework.stereotype.Repository

@Repository
class DeviceRepositoryImpl : DeviceRepository, AbstractRepository() {
    override fun add(deviceEntity: DeviceEntity) = Try { entityManager.persist(deviceEntity) }

    override fun find(deviceId: Long): Option<DeviceEntity> = entityManager.findBy<DeviceEntity, Long>(DeviceEntity.ID, deviceId).toOption()

    override fun delete(userId: Long) = Try { entityManager.remove<DeviceEntity, Long>(DeviceEntity.USER_ID, userId) }

    override fun update(deviceEntity: DeviceEntity) = Try { entityManager.merge(deviceEntity) }

    override fun delete(deviceEntity: DeviceEntity) = Try {
        entityManager.remove(if (entityManager.contains(deviceEntity)) deviceEntity else entityManager.merge(deviceEntity))
    }
}