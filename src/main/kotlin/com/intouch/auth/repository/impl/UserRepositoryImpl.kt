/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.repository.impl

import arrow.core.Try
import arrow.core.toOption
import com.intouch.auth.findBy
import com.intouch.auth.model.UserEntity
import com.intouch.auth.repository.AbstractRepository
import com.intouch.auth.repository.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl : UserRepository, AbstractRepository() {
    override fun find(name: String) = findEntity(name).toOption()

    private fun findEntity(name: String): UserEntity = entityManager.findBy(UserEntity.NAME, name)

    override fun find(id: Long) = findEntity(id).toOption()

    private fun findEntity(id: Long): UserEntity = entityManager.findBy(UserEntity.ID, id)

    override fun add(user: UserEntity) = Try { entityManager.persist(user) }

    override fun update(user: UserEntity) = Try { entityManager.merge(user) }
}
