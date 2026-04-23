package com.faithlink.core.repository

import com.faithlink.core.entity.Group
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface GroupRepository : JpaRepository<Group, UUID> {
    fun findByNameContainingIgnoreCase(name: String): List<Group>
    fun findByIsActiveTrue(): List<Group>
    fun findByLeaderId(leaderId: UUID): List<Group>
    fun findByChurchId(churchId: UUID): List<Group>
}
