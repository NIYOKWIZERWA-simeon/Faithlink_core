package com.faithlink.core.controller

import com.faithlink.core.dto.AuthResponse
import com.faithlink.core.dto.ChangePasswordRequest
import com.faithlink.core.dto.LoginRequest
import com.faithlink.core.dto.RefreshTokenRequest
import com.faithlink.core.dto.RegisterRequest
import com.faithlink.core.entity.User
import com.faithlink.core.security.JwtUtil
import com.faithlink.core.service.AuthService
import com.faithlink.core.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = ["*"])
class AuthController(
    private val authService: AuthService,
    private val userService: UserService,
    private val jwtUtil: JwtUtil
) {
    
    @PostMapping("/register")
    fun register(@Valid @RequestBody registerRequest: RegisterRequest): ResponseEntity<AuthResponse> {
        val authResponse = authService.register(registerRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse)
    }
    
    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<AuthResponse> {
        val authResponse = authService.login(loginRequest)
        return ResponseEntity.ok(authResponse)
    }
    
    @PostMapping("/refresh")
    fun refreshToken(@Valid @RequestBody refreshTokenRequest: RefreshTokenRequest): ResponseEntity<AuthResponse> {
        val authResponse = authService.refreshToken(refreshTokenRequest.token)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        return ResponseEntity.ok(authResponse)
    }
    
    @GetMapping("/profile")
    fun getProfile(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<User> {
        val user = authService.getCurrentUser(userDetails.username)
        return ResponseEntity.ok(user)
    }
    
    @PutMapping("/profile")
    fun updateProfile(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody updateUserRequest: com.faithlink.core.dto.UserUpdateRequest
    ): ResponseEntity<User> {
        val currentUser = authService.getCurrentUser(userDetails.username)
        val dummyUser = User(
            firstName = updateUserRequest.firstName,
            lastName = updateUserRequest.lastName,
            email = updateUserRequest.email,
            phone = updateUserRequest.phone,
            password = "",
            isActive = updateUserRequest.isActive
        )
        val updatedUser = userService.updateUser(currentUser.id!!, dummyUser)
        return updatedUser.map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }
    
    @PutMapping("/password")
    fun changePassword(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody changePasswordRequest: ChangePasswordRequest
    ): ResponseEntity<Void> {
        val currentUser = authService.getCurrentUser(userDetails.username)
        authService.changePassword(currentUser.id!!, changePasswordRequest)
        return ResponseEntity.ok().build()
    }
    
    @PostMapping("/logout")
    fun logout(): ResponseEntity<Map<String, String>> {
        // In a stateless JWT application, logout is typically handled client-side
        // by removing the token. This endpoint is for consistency.
        return ResponseEntity.ok(mapOf("message" to "Logout successful"))
    }
    
    @GetMapping("/validate")
    fun validateToken(@RequestHeader("Authorization") authorizationHeader: String): ResponseEntity<Map<String, Any>> {
        val token = authorizationHeader.substring(7) // Remove "Bearer " prefix
        
        return if (jwtUtil.validateToken(token)) {
            val username = jwtUtil.extractUsername(token)
            val roles = jwtUtil.extractRoles(token)
            ResponseEntity.ok(mapOf(
                "valid" to true,
                "username" to username,
                "roles" to roles
            ))
        } else {
            ResponseEntity.ok(mapOf("valid" to false))
        }
    }
}
