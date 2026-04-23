package com.faithlink.core.repository

import com.faithlink.core.entity.Announcement
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface AnnouncementRepository : JpaRepository<Announcement, UUID> {
    fun findByChurchId(churchId: UUID): List<Announcement>
    fun findByChurchIdAndExpiryDateAfterOrExpiryDateIsNull(churchId: UUID, now: LocalDateTime): List<Announcement>
}
