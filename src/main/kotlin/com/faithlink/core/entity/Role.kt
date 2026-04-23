package com.faithlink.core.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

@Entity
@Table(name = "roles")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @NotBlank(message = "Role name is required")
    @Size(max = 50, message = "Role name must not exceed 50 characters")
    @Column(name = "name", nullable = false, unique = true, length = 50)
    val name: String,
    
    @Size(max = 200, message = "Description must not exceed 200 characters")
    @Column(name = "description", length = 200)
    val description: String? = null
)
