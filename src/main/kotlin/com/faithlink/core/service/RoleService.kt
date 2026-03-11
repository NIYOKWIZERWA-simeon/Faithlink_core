package com.faithlink.core.service

import com.faithlink.core.entity.Role
import com.faithlink.core.repository.RoleRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class RoleService(
    private val roleRepository: RoleRepository
) {
    
    fun createRole(role: Role): Role {
        if (roleRepository.existsByName(role.name)) {
            throw IllegalArgumentException("Role with name ${role.name} already exists")
        }
        return roleRepository.save(role)
    }
    
    @Transactional(readOnly = true)
    fun getRoleById(id: Long): Optional<Role> {
        return roleRepository.findById(id)
    }
    
    @Transactional(readOnly = true)
    fun getRoleByName(name: String): Optional<Role> {
        return roleRepository.findByNameIgnoreCase(name)
    }
    
    @Transactional(readOnly = true)
    fun getAllRoles(pageable: Pageable): Page<Role> {
        return roleRepository.findAll(pageable)
    }
    
    @Transactional(readOnly = true)
    fun getAllRoles(): List<Role> {
        return roleRepository.findAll()
    }
    
    fun updateRole(id: Long, updatedRole: Role): Optional<Role> {
        return roleRepository.findById(id).map { existingRole ->
            val roleToUpdate = existingRole.copy(
                name = updatedRole.name,
                description = updatedRole.description
            )
            roleRepository.save(roleToUpdate)
        }
    }
    
    fun deleteRole(id: Long): Boolean {
        return if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id)
            true
        } else {
            false
        }
    }
    
    @Transactional(readOnly = true)
    fun searchRolesByName(name: String): List<Role> {
        return roleRepository.findByNameContaining(name)
    }
}
