/*
 * Copyright (c) 2018. This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.model

import com.intouch.auth.security.generateSecret
import com.intouch.microbase.config.DatabaseConfig.Companion.SCHEMA
import javax.persistence.*

@Entity
@Table(name = "device", schema = SCHEMA)
class DeviceEntity(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(name = "user_id")
        val userId: Long,

        @Column(name = "refresh_secret")
        var refreshSecret: String,

        @Column(name = "resource_secret")
        var resourceSecret: String
) {
    constructor(userId: Long) : this(0, userId, generateSecret(), generateSecret())

    companion object {
        const val ID = "id"
        const val USER_ID = "userId"
        const val REFRESH_SECRET = "refreshSecret"
        const val RESOURCE_SECRET = "resourceSecret"
    }
}