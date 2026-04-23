package com.faithlink.core.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class RoleCreateRequest(
    @field:NotBlank(message = "Role name is required")
    @field:Size(max = 50, message = "Role name must not exceed 50 characters")
    val name: String,
    
    @field:Size(max = 200, message = "Description must not exceed 200 characters")
    val description: String? = null
)

data class RoleUpdateRequest(
    @field:NotBlank(message = "Role name is required")
    @field:Size(max = 50, message = "Role name must not exceed 50 characters")
    val name: String,
    
    @field:Size(max = 200, message = "Description must not exceed 200 characters")
    val description: String? = null
)

data class RoleResponse(
    val id: UUID,
    val name: String,
    val description: String?
) {
    companion object {
        fun from(role: com.faithlink.core.entity.Role): RoleResponse {
            return RoleResponse(
                id = role.id!!,
                name = role.name,
                description = role.description
            )
        }
    }
}
