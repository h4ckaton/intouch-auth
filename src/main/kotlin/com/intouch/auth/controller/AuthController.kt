/*
 * Copyright (c) 2018. This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

package com.intouch.auth.controller

import com.intouch.auth.security.User
import com.intouch.auth.security.service.JwtTokenService
import com.intouch.auth.service.DeviceService
import com.intouch.auth.service.UserService
import com.intouch.lib.auth.AuthEndpoints.LOGIN
import com.intouch.lib.auth.AuthEndpoints.LOGOUT
import com.intouch.lib.auth.AuthEndpoints.LOGOUT_ALL
import com.intouch.lib.auth.AuthEndpoints.REGISTER
import com.intouch.lib.auth.AuthEndpoints.RESET_PASSWORD
import com.intouch.lib.auth.AuthEndpoints.REVOKE
import com.intouch.lib.auth.Roles
import com.intouch.lib.auth.dto.AuthenticationRequest
import com.intouch.lib.auth.dto.AuthenticationResponse
import com.intouch.lib.auth.dto.RegisterRequest
import com.intouch.lib.auth.dto.RegisterResponse
import com.intouch.microbase.postResponse
import com.intouch.microbase.putResponse
import com.intouch.microbase.security.SecurityConstants.REFRESH_TOKEN
import io.swagger.annotations.Api
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@Api("Auth Controller")
@RestController
class AuthController(private var userService: UserService,
                     private var jwtTokenService: JwtTokenService,
                     private var deviceService: DeviceService,
                     private var modelMapper: ModelMapper) {
    @PostMapping(REGISTER)
    fun register(@RequestBody registerRequest: RegisterRequest) =
            userService.register(registerRequest).let {
                modelMapper.postResponse<RegisterResponse>(it)
            }

    @PostMapping(LOGIN)
    fun login(@RequestBody authenticationRequest: AuthenticationRequest) {
    }

    @GetMapping("/auth/test")
    fun test(authentication: Authentication): String {
        val user = (authentication.principal as User)
        return "${user.userId}  ${user.deviceId}"
    }

    @GetMapping(REVOKE)
    @PreAuthorize("hasRole('$REFRESH_TOKEN')")
    fun revoke(authentication: Authentication): ResponseEntity<AuthenticationResponse> {
        val tokens = jwtTokenService.generateTokens(authentication)
        return ResponseEntity(AuthenticationResponse(tokens.first, tokens.second), HttpStatus.CREATED)
    }

    @PutMapping(LOGOUT)
    @PreAuthorize("hasRole('${Roles.USER}')")
    fun logout(authentication: Authentication) =
            putResponse(deviceService.logout((authentication.principal as User).deviceId))

    @PutMapping(LOGOUT_ALL)
    @PreAuthorize("hasRole('${Roles.USER}')")
    fun logoutAll(authentication: Authentication) =
            putResponse(deviceService.logoutAllDevices((authentication.principal as User).userId))

    @PostMapping(RESET_PASSWORD)
    fun resetPassword(mail: String) {
    }
}