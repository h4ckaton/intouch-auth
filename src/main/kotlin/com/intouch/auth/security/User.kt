/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class User(val userId: Long,
           val deviceId: Long,
           private val grantedAuthorities: List<GrantedAuthority>) : UserDetails {
    lateinit var name: String
    lateinit var passwordHash: String

    constructor(userEntity: com.intouch.auth.model.UserEntity, deviceId: Long) : this(userEntity.id, deviceId, userEntity.getAuthorities().toList()) {
        name = userEntity.name
        passwordHash = userEntity.passwordHash
    }


    override fun getUsername() = name

    override fun isCredentialsNonExpired() = true

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isEnabled() = true

    override fun getPassword() = passwordHash

    override fun getAuthorities() = grantedAuthorities
}