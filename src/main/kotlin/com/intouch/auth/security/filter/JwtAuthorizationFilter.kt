/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.security.filter

import arrow.core.Try
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.intouch.auth.security.User
import com.intouch.microbase.security.JwtTokenBody
import com.intouch.microbase.security.SecurityConstants.AUTHORIZATION_HEADER
import com.intouch.microbase.security.SecurityConstants.TOKEN_PREFIX
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilter(authManager: AuthenticationManager,
                             private val base64Decoder: Base64.Decoder) : BasicAuthenticationFilter(authManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        request.getHeader(AUTHORIZATION_HEADER).apply {
            if (this == null || !startsWith(TOKEN_PREFIX)) {
                chain.doFilter(request, response)
                return
            }
        }

        SecurityContextHolder.getContext().authentication = Try { getAuthentication(request) }.fold({
            // 401 client needs generate new tokens
            response.status = HttpStatus.UNAUTHORIZED.value()
            null
        }, { it }) ?: return
        chain.doFilter(request, response)
    }

    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val token = request.getHeader(AUTHORIZATION_HEADER).replace(TOKEN_PREFIX, "")
        val untrustedTokenBody = untrustedJwtTokenBody(token)
        val roles = untrustedTokenBody.roles.map { SimpleGrantedAuthority(it) }
        // we don' need check sign of jwt token, it's gateway's job

        return UsernamePasswordAuthenticationToken(User(untrustedTokenBody.userId, untrustedTokenBody.deviceId, roles), null, roles)
    }

    private fun untrustedJwtTokenBody(jwtToken: String): JwtTokenBody {
        val payload = jwtToken.split('.')[1]
        return jacksonObjectMapper().readValue(base64Decoder.decode(payload), JwtTokenBody::class.java)
    }
}