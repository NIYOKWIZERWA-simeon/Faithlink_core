package com.faithlink.core.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "announcements")
data class Announcement(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @Column(nullable = false, length = 200)
    var title: String,
    
    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,
    
    @Column(name = "expiry_date")
    var expiryDate: LocalDateTime? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id", nullable = false)
    var church: Church,
    
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
