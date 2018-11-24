/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.service.impl

import arrow.core.toOption
import com.intouch.auth.factory.UserEntityFactory
import com.intouch.auth.repository.UserRepository
import com.intouch.auth.service.UserService
import com.intouch.lib.auth.dto.RegisterRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(private val userRepository: UserRepository,
                      private val userEntityFactory: UserEntityFactory) : UserService {

    override fun resetPassword(mail: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Transactional
    override fun register(request: RegisterRequest) = userEntityFactory.create(request)
            .takeIf { userRepository.add(it).isSuccess() }.toOption()
}