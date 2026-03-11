package com.faithlink.core.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Entity
@Table(name = "churches")
data class Church(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @NotBlank(message = "Church name is required")
    @Size(max = 100, message = "Church name must not exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    val name: String,
    
    @Size(max = 500, message = "Address must not exceed 500 characters")
    @Column(name = "address", length = 500)
    val address: String? = null,
    
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Column(name = "phone", length = 20)
    val phone: String? = null,
    
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(name = "email", length = 100)
    val email: String? = null,
    
    @Column(name = "website", length = 200)
    val website: String? = null,
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(name = "description", columnDefinition = "TEXT")
    val description: String? = null,
    
    @Column(name = "is_active")
    val isActive: Boolean = true,
    
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
