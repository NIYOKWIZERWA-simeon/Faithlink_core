package com.faithlink.core.service

import com.faithlink.core.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional(readOnly = true)
class SyncService(
    private val churchRepository: ChurchRepository,
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository,
    private val donationRepository: DonationRepository,
    private val churchGroupRepository: ChurchGroupRepository,
    private val sermonRepository: SermonRepository,
    private val prayerRequestRepository: PrayerRequestRepository,
    private val announcementRepository: AnnouncementRepository
) {
    /**
     * Fetches all essential data for a specific church to bootstrap a local/offline client.
     */
    fun getBootstrapData(churchId: UUID): Map<String, Any> {
        val church = churchRepository.findById(churchId).orElseThrow { IllegalArgumentException("Church not found") }
        
        return mapOf(
            "church" to church,
            "users" to userRepository.findByChurchId(churchId),
            "events" to eventRepository.findByChurchId(churchId),
            "groups" to churchGroupRepository.findByChurchId(churchId),
            "sermons" to sermonRepository.findByChurchId(churchId),
            "announcements" to announcementRepository.findByChurchIdAndExpiryDateAfterOrExpiryDateIsNull(churchId, LocalDateTime.now())
        )
    }

    /**
     * Placeholder for delta sync - in a full implementation, this would use a 'last_updated' timestamp 
     * on all entities to return only what has changed.
     */
    fun getDeltaSync(churchId: UUID, lastSyncTime: LocalDateTime): Map<String, Any> {
        // Implementation would filter by updatedAt > lastSyncTime
        return emptyMap() 
    }
}
