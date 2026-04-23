package com.faithlink.core.repository

import com.faithlink.core.entity.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface EventRepository : JpaRepository<Event, UUID> {
    fun findByTitleContainingIgnoreCase(title: String): List<Event>
    fun findByStartTimeAfter(startTime: LocalDateTime): List<Event>
    fun findByStartTimeBetween(start: LocalDateTime, end: LocalDateTime): List<Event>
    fun findByChurchId(churchId: UUID): List<Event>
}
