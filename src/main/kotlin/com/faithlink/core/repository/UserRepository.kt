package com.faithlink.core.repository

import com.faithlink.core.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = ["roles", "church"])
    fun findByEmail(email: String): Optional<User>

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = ["roles", "church"])
    fun findByEmailIgnoreCase(email: String): Optional<User>
    fun existsByEmail(email: String): Boolean
    fun findByFirstNameContainingIgnoreCase(firstName: String): List<User>
    fun findByLastNameContainingIgnoreCase(lastName: String): List<User>
    
    @Query("SELECT u FROM User u WHERE u.isActive = true")
    fun findActiveUsers(): List<User>
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    fun findByRoleName(@Param("roleName") roleName: String): List<User>
    
    @Query("SELECT u FROM User u WHERE u.church.id = :churchId")
    fun findByChurchId(@Param("churchId") churchId: UUID): List<User>
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    fun countActiveUsers(): Long
}
