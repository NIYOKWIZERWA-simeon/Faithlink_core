package com.faithlink.core.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "user_groups")
data class UserGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    val group: Group,
    
    @Column(nullable = false, length = 20)
    var status: String = "ACTIVE", // e.g., ACTIVE, PENDING, INACTIVE
    
    @Column(name = "join_date", nullable = false)
    val joinDate: LocalDateTime = LocalDateTime.now()
)
