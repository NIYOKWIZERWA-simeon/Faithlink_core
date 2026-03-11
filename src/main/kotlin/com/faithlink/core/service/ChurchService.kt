package com.faithlink.core.service

import com.faithlink.core.entity.Church
import com.faithlink.core.repository.ChurchRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class ChurchService(
    private val churchRepository: ChurchRepository
) {
    
    fun createChurch(church: Church): Church {
        if (churchRepository.existsByName(church.name)) {
            throw IllegalArgumentException("Church with name ${church.name} already exists")
        }
        return churchRepository.save(church)
    }
    
    @Transactional(readOnly = true)
    fun getChurchById(id: Long): Optional<Church> {
        return churchRepository.findById(id)
    }
    
    @Transactional(readOnly = true)
    fun getChurchByName(name: String): Optional<Church> {
        return churchRepository.findByName(name)
    }
    
    @Transactional(readOnly = true)
    fun getAllChurches(pageable: Pageable): Page<Church> {
        return churchRepository.findAll(pageable)
    }
    
    @Transactional(readOnly = true)
    fun getActiveChurches(): List<Church> {
        return churchRepository.findActiveChurches()
    }
    
    fun updateChurch(id: Long, updatedChurch: Church): Optional<Church> {
        return churchRepository.findById(id).map { existingChurch ->
            val churchToUpdate = existingChurch.copy(
                name = updatedChurch.name,
                address = updatedChurch.address,
                phone = updatedChurch.phone,
                email = updatedChurch.email,
                website = updatedChurch.website,
                description = updatedChurch.description,
                isActive = updatedChurch.isActive
            )
            churchRepository.save(churchToUpdate)
        }
    }
    
    fun deleteChurch(id: Long): Boolean {
        return if (churchRepository.existsById(id)) {
            churchRepository.deleteById(id)
            true
        } else {
            false
        }
    }
    
    fun softDeleteChurch(id: Long): Boolean {
        return churchRepository.findById(id).map { church ->
            val deactivatedChurch = church.copy(isActive = false)
            churchRepository.save(deactivatedChurch)
            true
        }.orElse(false)
    }
    
    @Transactional(readOnly = true)
    fun searchChurchesByName(name: String): List<Church> {
        return churchRepository.findActiveChurchesByNameContaining(name)
    }
    
    @Transactional(readOnly = true)
    fun countActiveChurches(): Long {
        return churchRepository.countActiveChurches()
    }
}
