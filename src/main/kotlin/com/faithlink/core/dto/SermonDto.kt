package com.faithlink.core.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime
import java.util.UUID

data class SermonCreateRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(max = 200, message = "Title must not exceed 200 characters")
    val title: String,
    
    @field:Size(max = 100, message = "Preacher name must not exceed 100 characters")
    val preacherName: String? = null,
    
    val content: String? = null,
    val videoUrl: String? = null,
    val audioUrl: String? = null,
    val churchId: UUID,
    val sermonDate: LocalDateTime? = null
)

data class SermonResponse(
    val id: UUID,
    val title: String,
    val preacherName: String?,
    val content: String?,
    val videoUrl: String?,
    val audioUrl: String?,
    val churchId: UUID,
    val sermonDate: LocalDateTime,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(sermon: com.faithlink.core.entity.Sermon): SermonResponse {
            return SermonResponse(
                id = sermon.id!!,
                title = sermon.title,
                preacherName = sermon.preacherName,
                content = sermon.content,
                videoUrl = sermon.videoUrl,
                audioUrl = sermon.audioUrl,
                churchId = sermon.church.id!!,
                sermonDate = sermon.sermonDate,
                createdAt = sermon.createdAt
            )
        }
    }
}
