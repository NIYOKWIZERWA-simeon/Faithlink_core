package com.faithlink.core.service

import com.faithlink.core.entity.Group
import com.faithlink.core.repository.GroupRepository
import com.faithlink.core.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import java.util.Optional

@Service
@Transactional
class GroupService(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) {
    
    fun enrollMember(groupId: UUID, userId: UUID): Boolean {
        val group = groupRepository.findById(groupId).orElse(null) ?: return false
        val user = userRepository.findById(userId).orElse(null) ?: return false
        
        group.members.add(user)
        groupRepository.save(group)
        return true
    }

    @Transactional(readOnly = true)
    fun getGroupsByChurch(churchId: UUID): List<Group> {
        return groupRepository.findByChurchId(churchId)
    }

    fun createGroup(group: Group): Group {
        return groupRepository.save(group)
    }

    fun deleteGroup(id: UUID): Boolean {
        return if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id)
            true
        } else {
            false
        }
    }
    
    @Transactional(readOnly = true)
    fun getAllGroups(): List<Group> {
        return groupRepository.findAll()
    }
}
