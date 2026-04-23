package com.faithlink.core.service

import com.faithlink.core.entity.Sermon
import com.faithlink.core.repository.SermonRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class SermonService(
    private val sermonRepository: SermonRepository,
    private val churchRepository: com.faithlink.core.repository.ChurchRepository
) {
    fun createSermonFromDto(dto: com.faithlink.core.dto.SermonCreateRequest): Sermon {
        val church = churchRepository.findById(dto.churchId).orElseThrow { RuntimeException("Church not found") }
        val sermon = Sermon(
            title = dto.title,
            preacherName = dto.preacherName,
            content = dto.content,
            videoUrl = dto.videoUrl,
            audioUrl = dto.audioUrl,
            church = church,
            sermonDate = dto.sermonDate
        )
        return sermonRepository.save(sermon)
    }

    @Transactional(readOnly = true)
    fun getSermonsByChurch(churchId: UUID): List<Sermon> {
        return sermonRepository.findByChurchId(churchId)
    }

    fun createSermon(sermon: Sermon): Sermon {
        return sermonRepository.save(sermon)
    }

    fun deleteSermon(id: UUID): Boolean {
        return if (sermonRepository.existsById(id)) {
            sermonRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}
