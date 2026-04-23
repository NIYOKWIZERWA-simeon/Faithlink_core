package com.faithlink.core.repository

import com.faithlink.core.entity.Sermon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SermonRepository : JpaRepository<Sermon, UUID> {
    fun findByTitleContainingIgnoreCase(title: String): List<Sermon>
    fun findByPreacherNameContainingIgnoreCase(preacherName: String): List<Sermon>
    fun findByChurchId(churchId: UUID): List<Sermon>
}
