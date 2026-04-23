package com.faithlink.core.security

import com.faithlink.core.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {
    
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmailIgnoreCase(email)
            .orElseThrow { UsernameNotFoundException("User not found: $email") }
        
        return User.builder()
            .username(user.email)
            .password(user.password)
            .authorities(user.roles.map { org.springframework.security.core.authority.SimpleGrantedAuthority(if (it.name.startsWith("ROLE_")) it.name else "ROLE_${it.name}") })
            .accountExpired(!user.isActive)
            .accountLocked(!user.isActive)
            .credentialsExpired(false)
            .disabled(!user.isActive)
            .build()
    }
}
