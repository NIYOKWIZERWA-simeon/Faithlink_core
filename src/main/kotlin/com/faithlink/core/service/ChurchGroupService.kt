package com.faithlink.core.service

import com.faithlink.core.entity.ChurchGroup
import com.faithlink.core.repository.ChurchGroupRepository
import com.faithlink.core.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class ChurchGroupService(
    private val churchGroupRepository: ChurchGroupRepository,
    private val userRepository: UserRepository
) {
    
    fun enrollMember(groupId: UUID, userId: UUID): Boolean {
        val group = churchGroupRepository.findById(groupId).orElse(null) ?: return false
        val user = userRepository.findById(userId).orElse(null) ?: return false
        
        group.members.add(user)
        churchGroupRepository.save(group)
        return true
    }

    @Transactional(readOnly = true)
    fun getGroupsByChurch(churchId: UUID): List<ChurchGroup> {
        return churchGroupRepository.findByChurchId(churchId)
    }

    fun createGroup(group: ChurchGroup): ChurchGroup {
        return churchGroupRepository.save(group)
    }

    fun deleteGroup(id: UUID): Boolean {
        return if (churchGroupRepository.existsById(id)) {
            churchGroupRepository.deleteById(id)
            true
        } else {
            false
        }
    }
    
    @Transactional(readOnly = true)
    fun getAllGroups(): List<ChurchGroup> {
        return churchGroupRepository.findAll()
    }
}
