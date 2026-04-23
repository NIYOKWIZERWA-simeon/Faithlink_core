package com.faithlink.core.service

import com.faithlink.core.dto.AuthResponse
import com.faithlink.core.dto.ChangePasswordRequest
import com.faithlink.core.dto.LoginRequest
import com.faithlink.core.dto.RegisterRequest
import com.faithlink.core.entity.Role
import com.faithlink.core.entity.User
import com.faithlink.core.exception.AuthenticationException
import com.faithlink.core.exception.DuplicateResourceException
import com.faithlink.core.repository.RoleRepository
import com.faithlink.core.repository.UserRepository
import com.faithlink.core.security.JwtUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val churchRepository: com.faithlink.core.repository.ChurchRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {
    
    fun register(registerRequest: RegisterRequest): AuthResponse {
        // Check if user already exists
        if (userRepository.existsByEmail(registerRequest.email)) {
            throw DuplicateResourceException("User with email ${registerRequest.email} already exists")
        }
        
        // Get or create default MEMBER role
        val userRole = roleRepository.findByNameIgnoreCase("ROLE_MEMBER")
            .orElseGet {
                roleRepository.save(Role(name = "ROLE_MEMBER", description = "Regular member role"))
            }

        // Get church context
        val church = registerRequest.churchId?.let { 
            churchRepository.findById(it).orElse(null) 
        }
        
        // Create new user
        val user = User(
            firstName = registerRequest.firstName,
            lastName = registerRequest.lastName,
            email = registerRequest.email,
            password = passwordEncoder.encode(registerRequest.password) ?: "",
            phone = registerRequest.phone,
            isActive = true,
            church = church,
            roles = mutableSetOf(userRole)
        )
        
        val savedUser = userRepository.save(user)
        
        // Generate JWT token
        val token = jwtUtil.generateToken(savedUser.email, savedUser.roles.map { it.name }.toSet())
        
        return AuthResponse(
            token = token,
            expiresIn = 86400000L, // 24 hours
            user = com.faithlink.core.dto.UserSummary.from(savedUser)
        )
    }
    
    fun login(loginRequest: LoginRequest): AuthResponse {
        try {
            val authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
            )
            
            val user = userRepository.findByEmail(loginRequest.email)
                .orElseThrow { UsernameNotFoundException("User not found") }
            
            if (!user.isActive) {
                throw AuthenticationException("User account is deactivated")
            }
            
            val token = jwtUtil.generateToken(user.email, user.roles.map { it.name }.toSet())
            
            return AuthResponse(
                token = token,
                expiresIn = 86400000L, // 24 hours
                user = com.faithlink.core.dto.UserSummary.from(user)
            )
        } catch (e: Exception) {
            println("AUTH ERROR: ${e.message}")
            e.printStackTrace()
            throw AuthenticationException("Invalid email or password")
        }
    }
    
    fun refreshToken(token: String): AuthResponse? {
        val newToken = jwtUtil.refreshToken(token) ?: return null
        
        val email = jwtUtil.extractUsername(newToken)
        val user = userRepository.findByEmail(email)
            .orElseThrow { UsernameNotFoundException("User not found") }
        
        return AuthResponse(
            token = newToken,
            expiresIn = 86400000L, // 24 hours
            user = com.faithlink.core.dto.UserSummary.from(user)
        )
    }
    
    fun changePassword(userId: UUID, changePasswordRequest: ChangePasswordRequest) {
        val user = userRepository.findById(userId)
            .orElseThrow { UsernameNotFoundException("User not found") }
        
        // Verify current password
        if (!passwordEncoder.matches(changePasswordRequest.currentPassword, user.password)) {
            throw AuthenticationException("Current password is incorrect")
        }
        
        // Verify new passwords match
        if (changePasswordRequest.newPassword != changePasswordRequest.confirmPassword) {
            throw AuthenticationException("New passwords do not match")
        }
        
        // Update password
        val updatedUser = user.copy(
            password = passwordEncoder.encode(changePasswordRequest.newPassword) ?: "",
            updatedAt = LocalDateTime.now()
        )
        
        userRepository.save(updatedUser)
    }
    
    fun getCurrentUser(email: String): User {
        return userRepository.findByEmail(email)
            .orElseThrow { UsernameNotFoundException("User not found") }
    }
}
