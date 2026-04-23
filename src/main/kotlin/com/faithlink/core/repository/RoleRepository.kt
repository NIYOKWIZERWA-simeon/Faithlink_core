package com.faithlink.core.repository

import com.faithlink.core.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<Role, UUID> {
    fun findByName(name: String): Optional<Role>
    fun findByNameIgnoreCase(name: String): Optional<Role>
    fun existsByName(name: String): Boolean
    
    @Query("SELECT r FROM Role r WHERE r.name LIKE %:name%")
    fun findByNameContaining(@Param("name") name: String): List<Role>
}
