package com.faithlink.core.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "sermons")
data class Sermon(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @Column(nullable = false, length = 200)
    var title: String,
    
    @Column(name = "preacher_name", length = 100)
    var preacherName: String? = null,
    
    @Column(columnDefinition = "TEXT")
    var content: String? = null,
    
    @Column(name = "video_url", length = 500)
    var videoUrl: String? = null,
    
    @Column(name = "audio_url", length = 500)
    var audioUrl: String? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id", nullable = false)
    var church: Church,
    
    @Column(name = "sermon_date")
    var sermonDate: LocalDateTime = LocalDateTime.now(),
    
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
