package com.faithlink.core.repository

import com.faithlink.core.entity.UserGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserGroupRepository : JpaRepository<UserGroup, UUID> {
    fun findByUserId(userId: UUID): List<UserGroup>
    fun findByGroupId(groupId: UUID): List<UserGroup>
    fun existsByUserIdAndGroupId(userId: UUID, groupId: UUID): Boolean
    fun deleteByUserIdAndGroupId(userId: UUID, groupId: UUID)
}
