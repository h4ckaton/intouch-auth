/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.security

import com.intouch.auth.model.DeviceEntity
import com.intouch.auth.model.UserEntity
import org.springframework.security.core.userdetails.UserDetails

class User(val userEntity: UserEntity,
           val deviceEntity: DeviceEntity) : UserDetails {
    override fun getUsername() = userEntity.name

    override fun isCredentialsNonExpired() = true

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun getAuthorities() = userEntity.getAuthorities()

    override fun isEnabled() = userEntity.enabled

    override fun getPassword() = userEntity.passwordHash
}