package com.faithlink.core.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime
import java.util.UUID

data class GroupCreateRequest(
    @field:NotBlank(message = "Group name is required")
    @field:Size(max = 100, message = "Group name must not exceed 100 characters")
    val name: String,
    
    @field:Size(max = 1000, message = "Description must not exceed 1000 characters")
    val description: String? = null,
    
    val leaderId: UUID? = null,
    
    val churchId: UUID
)

data class GroupResponse(
    val id: UUID,
    val name: String,
    val description: String?,
    val leaderId: UUID?,
    val churchId: UUID,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(group: com.faithlink.core.entity.Group): GroupResponse {
            return GroupResponse(
                id = group.id!!,
                name = group.name,
                description = group.description,
                leaderId = group.leader?.id,
                churchId = group.church.id!!,
                isActive = group.isActive,
                createdAt = group.createdAt,
                updatedAt = group.updatedAt
            )
        }
    }
}
