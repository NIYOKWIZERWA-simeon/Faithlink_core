package com.faithlink.core.service

import com.faithlink.core.entity.User
import com.faithlink.core.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository
) {
    
    fun createUser(user: User): User {
        if (userRepository.existsByEmail(user.email)) {
            throw IllegalArgumentException("User with email ${user.email} already exists")
        }
        return userRepository.save(user)
    }
    
    @Transactional(readOnly = true)
    fun getUserById(id: Long): Optional<User> {
        return userRepository.findById(id)
    }
    
    @Transactional(readOnly = true)
    fun getUserByEmail(email: String): Optional<User> {
        return userRepository.findByEmailIgnoreCase(email)
    }
    
    @Transactional(readOnly = true)
    fun getAllUsers(pageable: Pageable): Page<User> {
        return userRepository.findAll(pageable)
    }
    
    @Transactional(readOnly = true)
    fun getActiveUsers(): List<User> {
        return userRepository.findActiveUsers()
    }
    
    fun updateUser(id: Long, updatedUser: User): Optional<User> {
        return userRepository.findById(id).map { existingUser ->
            val userToUpdate = existingUser.copy(
                firstName = updatedUser.firstName,
                lastName = updatedUser.lastName,
                email = updatedUser.email,
                phone = updatedUser.phone,
                isActive = updatedUser.isActive
            )
            userRepository.save(userToUpdate)
        }
    }
    
    fun deleteUser(id: Long): Boolean {
        return if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
            true
        } else {
            false
        }
    }
    
    fun softDeleteUser(id: Long): Boolean {
        return userRepository.findById(id).map { user ->
            val deactivatedUser = user.copy(isActive = false)
            userRepository.save(deactivatedUser)
            true
        }.orElse(false)
    }
    
    @Transactional(readOnly = true)
    fun searchUsersByName(firstName: String?, lastName: String?): List<User> {
        return when {
            firstName != null && lastName != null -> {
                val firstNames = userRepository.findByFirstNameContainingIgnoreCase(firstName)
                val lastNames = userRepository.findByLastNameContainingIgnoreCase(lastName)
                firstNames.intersect(lastNames.toSet()).toList()
            }
            firstName != null -> userRepository.findByFirstNameContainingIgnoreCase(firstName)
            lastName != null -> userRepository.findByLastNameContainingIgnoreCase(lastName)
            else -> emptyList()
        }
    }
    
    @Transactional(readOnly = true)
    fun countActiveUsers(): Long {
        return userRepository.countActiveUsers()
    }
    
    @Transactional(readOnly = true)
    fun isOwner(userId: Long, email: String): Boolean {
        return userRepository.findById(userId)
            .map { user -> user.email == email }
            .orElse(false)
    }
}
