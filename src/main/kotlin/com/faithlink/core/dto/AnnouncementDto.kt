package com.faithlink.core.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime
import java.util.UUID

data class AnnouncementRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(max = 200, message = "Title must not exceed 200 characters")
    val title: String,
    
    @field:NotBlank(message = "Content is required")
    val content: String,
    
    val expiresAt: LocalDateTime? = null,
    val churchId: UUID
)

data class AnnouncementResponse(
    val id: UUID,
    val title: String,
    val content: String,
    val expiresAt: LocalDateTime?,
    val churchId: UUID,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(announcement: com.faithlink.core.entity.Announcement): AnnouncementResponse {
            return AnnouncementResponse(
                id = announcement.id!!,
                title = announcement.title,
                content = announcement.content,
                expiresAt = announcement.expiryDate,
                churchId = announcement.church.id!!,
                createdAt = announcement.createdAt
            )
        }
    }
}
