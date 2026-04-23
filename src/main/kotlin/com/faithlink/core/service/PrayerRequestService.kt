package com.faithlink.core.service

import com.faithlink.core.entity.PrayerRequest
import com.faithlink.core.repository.PrayerRequestRepository
import com.faithlink.core.repository.UserRepository
import com.faithlink.core.repository.ChurchRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class PrayerRequestService(
    private val prayerRequestRepository: PrayerRequestRepository,
    private val userRepository: UserRepository,
    private val churchRepository: ChurchRepository
) {
    fun submitRequest(userId: UUID, churchId: UUID, subject: String, content: String, isAnonymous: Boolean): PrayerRequest {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("User not found") }
        val church = churchRepository.findById(churchId).orElseThrow { IllegalArgumentException("Church not found") }
        
        val request = PrayerRequest(
            user = user,
            church = church,
            subject = subject,
            content = content,
            isAnonymous = isAnonymous
        )
        return prayerRequestRepository.save(request)
    }

    @Transactional(readOnly = true)
    fun getRequestsByChurch(churchId: UUID): List<PrayerRequest> {
        return prayerRequestRepository.findByChurchId(churchId)
    }

    @Transactional(readOnly = true)
    fun getPublicRequestsByChurch(churchId: UUID): List<PrayerRequest> {
        return prayerRequestRepository.findByChurchIdAndIsAnonymousFalse(churchId)
    }

    fun respondToRequest(requestId: UUID, response: String): PrayerRequest {
        val request = prayerRequestRepository.findById(requestId).orElseThrow { IllegalArgumentException("Request not found") }
        request.response = response
        request.status = "PRAYED"
        return prayerRequestRepository.save(request)
    }
}
