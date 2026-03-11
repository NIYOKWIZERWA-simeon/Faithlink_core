package com.faithlink.core.repository

import com.faithlink.core.entity.Church
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChurchRepository : JpaRepository<Church, Long> {
    fun findByName(name: String): Optional<Church>
    fun findByNameContainingIgnoreCase(name: String): List<Church>
    fun existsByName(name: String): Boolean
    fun findByEmail(email: String): Optional<Church>
    fun findByPhone(phone: String): Optional<Church>
    
    @Query("SELECT c FROM Church c WHERE c.isActive = true")
    fun findActiveChurches(): List<Church>
    
    @Query("SELECT c FROM Church c WHERE c.name LIKE %:name% AND c.isActive = true")
    fun findActiveChurchesByNameContaining(@Param("name") name: String): List<Church>
    
    @Query("SELECT COUNT(c) FROM Church c WHERE c.isActive = true")
    fun countActiveChurches(): Long
}
