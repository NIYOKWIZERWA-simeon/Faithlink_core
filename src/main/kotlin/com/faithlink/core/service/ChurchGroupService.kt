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
    private val userRepository: UserRepository,
    private val churchRepository: com.faithlink.core.repository.ChurchRepository,
    private val userGroupRepository: com.faithlink.core.repository.UserGroupRepository
) {
    
    fun createGroupFromDto(dto: com.faithlink.core.dto.ChurchGroupCreateRequest): ChurchGroup {
        val church = churchRepository.findById(dto.churchId)
            .orElseThrow { RuntimeException("Church not found") }
            
        val leader = dto.leaderId?.let { 
            userRepository.findById(it).orElse(null) 
        }

        val group = ChurchGroup(
            name = dto.name,
            description = dto.description,
            leader = leader,
            church = church
        )
        return churchGroupRepository.save(group)
    }

    fun enrollMember(groupId: UUID, userId: UUID): Boolean {
        if (userGroupRepository.existsByUserIdAndGroupId(userId, groupId)) return false
        
        val group = churchGroupRepository.findById(groupId).orElse(null) ?: return false
        val user = userRepository.findById(userId).orElse(null) ?: return false
        
        val userGroup = com.faithlink.core.entity.UserGroup(
            user = user,
            group = group,
            status = "ACTIVE"
        )
        userGroupRepository.save(userGroup)
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
