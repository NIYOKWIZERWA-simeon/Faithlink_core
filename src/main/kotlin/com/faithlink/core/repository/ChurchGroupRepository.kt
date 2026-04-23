package com.faithlink.core.repository

import com.faithlink.core.entity.ChurchGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ChurchGroupRepository : JpaRepository<ChurchGroup, UUID> {
    fun findByNameContainingIgnoreCase(name: String): List<ChurchGroup>
    fun findByIsActiveTrue(): List<ChurchGroup>
    fun findByLeaderId(leaderId: UUID): List<ChurchGroup>
    fun findByChurchId(churchId: UUID): List<ChurchGroup>
}
