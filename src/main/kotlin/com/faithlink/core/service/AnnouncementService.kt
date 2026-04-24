package com.faithlink.core.service

import com.faithlink.core.dto.AnnouncementRequest
import com.faithlink.core.dto.AnnouncementResponse
import com.faithlink.core.entity.Announcement
import com.faithlink.core.repository.AnnouncementRepository
import com.faithlink.core.repository.ChurchRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class AnnouncementService(
    private val announcementRepository: AnnouncementRepository,
    private val churchRepository: ChurchRepository
) {
    fun createAnnouncement(dto: AnnouncementRequest): AnnouncementResponse {
        val church = churchRepository.findById(dto.churchId)
            .orElseThrow { RuntimeException("Church not found") }
            
        val announcement = Announcement(
            title = dto.title,
            content = dto.content,
            expiryDate = dto.expiresAt,
            church = church
        )
        
        val saved = announcementRepository.save(announcement)
        return AnnouncementResponse.from(saved)
    }

    @Transactional(readOnly = true)
    fun getAnnouncementsByChurch(churchId: UUID): List<AnnouncementResponse> {
        return announcementRepository.findByChurchId(churchId)
            .map { AnnouncementResponse.from(it) }
    }

    fun deleteAnnouncement(id: UUID) {
        announcementRepository.deleteById(id)
    }
}
