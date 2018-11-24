package com.intouch.auth.security.filter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.intouch.auth.security.service.JwtTokenService
import com.intouch.lib.auth.dto.AuthenticationRequest
import com.intouch.lib.auth.dto.AuthenticationResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(path: String, authenticationManager: AuthenticationManager, private val jwtTokenService: JwtTokenService)
    : AbstractAuthenticationProcessingFilter(AntPathRequestMatcher(path)) {
    init {
        setAuthenticationManager(authenticationManager)
    }

    override fun attemptAuthentication(req: HttpServletRequest, res: HttpServletResponse): Authentication? {
        val request: AuthenticationRequest = jacksonObjectMapper().readValue(req.inputStream)
        return authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.name, request.password, null))
    }

    override fun successfulAuthentication(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain, auth: Authentication) {
        val tokens = jwtTokenService.generateTokens(auth)
        res.addHeader("content-type", "application/json;charset=UTF-8")
        res.writer.apply {
            write(jacksonObjectMapper().writeValueAsString(AuthenticationResponse(tokens.first, tokens.second)))
            flush()
            close()
        }
    }
}