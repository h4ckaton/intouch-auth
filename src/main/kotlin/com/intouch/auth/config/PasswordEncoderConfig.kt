/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.security.SecureRandom

@Configuration
class PasswordEncoderConfig {
    companion object {
        private const val BCRYPT_ITERATIONS = 12
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder(BCRYPT_ITERATIONS)

    @Bean
    fun secureRandom(): SecureRandom = SecureRandom.getInstanceStrong()
}