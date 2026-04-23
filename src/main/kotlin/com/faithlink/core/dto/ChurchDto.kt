package com.faithlink.core.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime
import java.util.UUID

data class ChurchCreateRequest(
    @field:NotBlank(message = "Church name is required")
    @field:Size(max = 100, message = "Church name must not exceed 100 characters")
    val name: String,
    
    @field:Size(max = 500, message = "Address must not exceed 500 characters")
    val address: String? = null,
    
    @field:Size(max = 20, message = "Phone must not exceed 20 characters")
    val phone: String? = null,
    
    @field:Email(message = "Email should be valid")
    @field:Size(max = 100, message = "Email must not exceed 100 characters")
    val email: String? = null,
    
    @field:Size(max = 200, message = "Website must not exceed 200 characters")
    val website: String? = null,
    
    @field:Size(max = 1000, message = "Description must not exceed 1000 characters")
    val description: String? = null
)

data class ChurchUpdateRequest(
    @field:NotBlank(message = "Church name is required")
    @field:Size(max = 100, message = "Church name must not exceed 100 characters")
    val name: String,
    
    @field:Size(max = 500, message = "Address must not exceed 500 characters")
    val address: String? = null,
    
    @field:Size(max = 20, message = "Phone must not exceed 20 characters")
    val phone: String? = null,
    
    @field:Email(message = "Email should be valid")
    @field:Size(max = 100, message = "Email must not exceed 100 characters")
    val email: String? = null,
    
    @field:Size(max = 200, message = "Website must not exceed 200 characters")
    val website: String? = null,
    
    @field:Size(max = 1000, message = "Description must not exceed 1000 characters")
    val description: String? = null,
    
    val isActive: Boolean = true
)

data class ChurchResponse(
    val id: UUID,
    val name: String,
    val address: String?,
    val phone: String?,
    val email: String?,
    val website: String?,
    val description: String?,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(church: com.faithlink.core.entity.Church): ChurchResponse {
            return ChurchResponse(
                id = church.id!!,
                name = church.name,
                address = church.address,
                phone = church.phone,
                email = church.email,
                website = church.website,
                description = church.description,
                isActive = church.isActive,
                createdAt = church.createdAt,
                updatedAt = church.updatedAt
            )
        }
    }
}

data class ChurchSummary(
    val id: UUID,
    val name: String,
    val email: String?,
    val phone: String?,
    val isActive: Boolean
) {
    companion object {
        fun from(church: com.faithlink.core.entity.Church): ChurchSummary {
            return ChurchSummary(
                id = church.id!!,
                name = church.name,
                email = church.email,
                phone = church.phone,
                isActive = church.isActive
            )
        }
    }
}
