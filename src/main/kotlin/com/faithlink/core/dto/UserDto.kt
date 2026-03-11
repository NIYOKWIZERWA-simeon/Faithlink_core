package com.faithlink.core.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class UserCreateRequest(
    @field:NotBlank(message = "First name is required")
    @field:Size(max = 50, message = "First name must not exceed 50 characters")
    val firstName: String,
    
    @field:NotBlank(message = "Last name is required")
    @field:Size(max = 50, message = "Last name must not exceed 50 characters")
    val lastName: String,
    
    @field:Email(message = "Email should be valid")
    @field:NotBlank(message = "Email is required")
    val email: String,
    
    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String,
    
    @field:Size(max = 20, message = "Phone must not exceed 20 characters")
    val phone: String? = null,
    
    val roleIds: Set<Long> = emptySet()
)

data class UserUpdateRequest(
    @field:NotBlank(message = "First name is required")
    @field:Size(max = 50, message = "First name must not exceed 50 characters")
    val firstName: String,
    
    @field:NotBlank(message = "Last name is required")
    @field:Size(max = 50, message = "Last name must not exceed 50 characters")
    val lastName: String,
    
    @field:Email(message = "Email should be valid")
    @field:NotBlank(message = "Email is required")
    val email: String,
    
    @field:Size(max = 20, message = "Phone must not exceed 20 characters")
    val phone: String? = null,
    
    val isActive: Boolean = true,
    
    val roleIds: Set<Long> = emptySet()
)

data class UserResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String?,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val roles: Set<RoleResponse>
) {
    companion object {
        fun from(user: com.faithlink.core.entity.User): UserResponse {
            return UserResponse(
                id = user.id!!,
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                phone = user.phone,
                isActive = user.isActive,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt,
                roles = user.roles.map { RoleResponse.from(it) }.toSet()
            )
        }
    }
}

data class UserSummary(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val isActive: Boolean
) {
    companion object {
        fun from(user: com.faithlink.core.entity.User): UserSummary {
            return UserSummary(
                id = user.id!!,
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                isActive = user.isActive
            )
        }
    }
}
