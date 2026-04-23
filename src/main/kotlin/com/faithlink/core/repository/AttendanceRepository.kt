package com.faithlink.core.repository

import com.faithlink.core.entity.Attendance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AttendanceRepository : JpaRepository<Attendance, UUID> {
    fun findByEventId(eventId: UUID): List<Attendance>
    fun findByUserId(userId: UUID): List<Attendance>
    fun existsByEventIdAndUserId(eventId: UUID, userId: UUID): Boolean
}
