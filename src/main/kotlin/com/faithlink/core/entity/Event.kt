package com.faithlink.core.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "events")
data class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @Column(nullable = false, length = 200)
    var title: String,
    
    @Column(length = 2000)
    var description: String? = null,
    
    @Column(nullable = false)
    var location: String,
    
    @Column(name = "start_time", nullable = false)
    var startTime: LocalDateTime,
    
    @Column(name = "end_time", nullable = false)
    var endTime: LocalDateTime,
    
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
