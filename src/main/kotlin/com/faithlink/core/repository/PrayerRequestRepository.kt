package com.faithlink.core.repository

import com.faithlink.core.entity.PrayerRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PrayerRequestRepository : JpaRepository<PrayerRequest, UUID> {
    fun findByUserId(userId: UUID): List<PrayerRequest>
    fun findByChurchId(churchId: UUID): List<PrayerRequest>
    fun findByStatus(status: String): List<PrayerRequest>
    fun findByChurchIdAndIsAnonymousFalse(churchId: UUID): List<PrayerRequest>
}
