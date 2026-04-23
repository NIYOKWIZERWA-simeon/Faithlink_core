package com.faithlink.core.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "prayer_requests")
data class PrayerRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    
    @Column(nullable = false, length = 200)
    var subject: String,
    
    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,
    
    @Column(length = 2000)
    var response: String? = null,
    
    @Column(nullable = false, length = 20)
    var status: String = "PENDING", // PENDING, PRAYED, TESTIMONY
    
    @Column(name = "is_anonymous")
    var isAnonymous: Boolean = false,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id", nullable = false)
    var church: Church,
    
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @PreUpdate
    fun onPreUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
