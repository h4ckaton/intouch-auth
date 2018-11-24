/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.model

import com.intouch.microbase.config.DatabaseConfig.Companion.SCHEMA
import org.springframework.security.core.GrantedAuthority
import javax.persistence.*

@Entity
@Table(name = "role", schema = SCHEMA)
data class RoleEntity(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(name = "name")
        val name: String,

        @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        @JoinTable(name = "role_privilege",
                joinColumns = [JoinColumn(name = "role_id")],
                inverseJoinColumns = [JoinColumn(name = "privilege_id")])
        val privileges: MutableSet<PrivilegeEntity>
) : GrantedAuthority {
    companion object {
        const val ID = "id"
        const val NAME = "name"
    }

    override fun getAuthority() = name

    override fun toString() = authority
}
