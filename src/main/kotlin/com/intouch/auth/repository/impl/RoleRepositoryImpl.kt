/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.repository.impl

import arrow.core.toOption
import com.intouch.auth.model.RoleEntity
import com.intouch.auth.repository.RoleRepository
import com.intouch.microbase.findBy
import com.intouch.microbase.repository.AbstractRepository
import org.springframework.stereotype.Repository

@Repository
class RoleRepositoryImpl : RoleRepository, AbstractRepository() {
    override fun find(username: String) = findEntity(username).toOption()

    private fun findEntity(username: String): RoleEntity = entityManager.findBy(RoleEntity.NAME, username)
}