package com.faithlink.core.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil
) : OncePerRequestFilter() {
    
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")
        
        var username: String? = null
        var jwt: String? = null
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7)
            username = jwtUtil.extractUsername(jwt)
        }
        
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            if (jwtUtil.validateToken(jwt!!)) {
                val roles = jwtUtil.extractRoles(jwt)
                val authorities = roles.map { SimpleGrantedAuthority("ROLE_$it") }
                
                val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities
                )
                usernamePasswordAuthenticationToken.details = 
                    WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }
        
        filterChain.doFilter(request, response)
    }
}
