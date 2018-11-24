/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.model

import com.intouch.auth.model.DeviceEntity.Companion.USER_ID
import com.intouch.auth.model.UserEntity.Companion.TABLE
import org.springframework.security.core.GrantedAuthority
import javax.persistence.*

@Entity
@Table(name = TABLE)
class UserEntity(
        @Id
        @Column(name = ID)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(name = EMAIL)
        var email: String,

        @Column(name = NAME)
        val name: String,

        @Column(name = HASH)
        var passwordHash: String,

        @Column(name = ENABLED)
        var enabled: Boolean,

        @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        @JoinTable(name = "user_role",
                joinColumns = [JoinColumn(name = "user_id")],
                inverseJoinColumns = [JoinColumn(name = "role_id")])
        val roles: MutableSet<RoleEntity>,

        @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
        @JoinColumn(name = USER_ID)
        val devices: MutableSet<DeviceEntity>
) {
    constructor(username: String, email: String) : this(0, email, username, "", true, HashSet<RoleEntity>(), HashSet<DeviceEntity>())

    fun getAuthorities() = HashSet<GrantedAuthority>().apply {
        roles.forEach {
            add(it)
            addAll(it.privileges)
        }
    }

    companion object {
        const val TABLE = "_user"
        const val ID = "id"
        const val EMAIL = "email"
        const val NAME = "name"
        const val HASH = "password_hash"
        const val ENABLED = "enabled"
    }
}