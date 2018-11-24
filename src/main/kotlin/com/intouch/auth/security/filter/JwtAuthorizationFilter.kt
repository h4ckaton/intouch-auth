/*
 * Copyright (c) 2018 This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.security.filter

import arrow.core.Try
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.intouch.auth.repository.DeviceRepository
import com.intouch.auth.repository.UserRepository
import com.intouch.auth.security.JwtTokenBody
import com.intouch.auth.security.SecurityConstants.AUTHORIZATION_HEADER
import com.intouch.auth.security.SecurityConstants.ROLE_REFRESH_TOKEN
import com.intouch.auth.security.SecurityConstants.TOKEN_PREFIX
import com.intouch.auth.security.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilter(authManager: AuthenticationManager,
                             private val base64Decoder: Base64.Decoder,
                             private val userRepository: UserRepository,
                             private val deviceRepository: DeviceRepository) : BasicAuthenticationFilter(authManager) {

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
        // it's only way to get proper secret using in parsing
        val untrustedTokenBody = untrustedJwtTokenBody(token)
        val device = deviceRepository.find(untrustedTokenBody.deviceId).fold({ return null }, { it })
        val signingKey = if (untrustedTokenBody.roles.contains(ROLE_REFRESH_TOKEN)) device.refreshSecret else device.resourceSecret

        // if there wasn't any exceptions then parsed token is trusted
        Jwts.parser()
                .setSigningKey(SecretKeySpec(signingKey.toByteArray(), SignatureAlgorithm.HS512.jcaName))
                .parseClaimsJws(token)

        return userRepository.find(device.userId).fold({ return null }, { it }).run {
            UsernamePasswordAuthenticationToken(User(this, device), passwordHash, untrustedTokenBody.roles.map { SimpleGrantedAuthority(it) })
        }
    }

    private fun untrustedJwtTokenBody(jwtToken: String): JwtTokenBody {
        val payload = jwtToken.split('.')[1]
        return jacksonObjectMapper().readValue(base64Decoder.decode(payload), JwtTokenBody::class.java)
    }
}