/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.intouch.auth.repository.DeviceRepository
import com.intouch.auth.repository.UserRepository
import com.intouch.auth.security.filter.JwtAuthenticationFilter
import com.intouch.auth.security.filter.JwtAuthorizationFilter
import com.intouch.auth.security.service.JwtTokenService
import com.intouch.lib.auth.AuthEndpoints.LOGIN
import com.intouch.lib.auth.AuthEndpoints.REGISTER
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(
        val userRepository: UserRepository,
        val jwtTokenService: JwtTokenService,
        val deviceRepository: DeviceRepository,
        val userDetailsService: UserDetailsService,
        val passwordEncoder: PasswordEncoder) : WebSecurityConfigurerAdapter() {
    companion object {
        private val AUTH_WHITELIST = arrayOf("/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**", "/$REGISTER")
    }

    override fun configure(http: HttpSecurity) {
        val authorizationFilter = JwtAuthorizationFilter(authenticationManager(), base64Decoder(), userRepository, deviceRepository)
        val authenticationFilter = JwtAuthenticationFilter("/$LOGIN", authenticationManager(), jwtTokenService)
        http.cors().and().csrf().disable().authorizeRequests().antMatchers(*AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authenticationProvider())
    }

    @Bean
    fun authenticationProvider() = DaoAuthenticationProvider().apply {
        setUserDetailsService(userDetailsService)
        setPasswordEncoder(passwordEncoder)
    }

    @Bean
    fun authManager(): AuthenticationManager = authenticationManager()

    @Bean
    fun base64Decoder(): Base64.Decoder = Base64.getDecoder()

    @Bean
    @Primary
    fun jsonObjectMapper(): ObjectMapper = Jackson2ObjectMapperBuilder.json()
            .build()

    @Bean
    fun corsConfigurationSource() = UrlBasedCorsConfigurationSource().apply {
        registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues())
    }
}
