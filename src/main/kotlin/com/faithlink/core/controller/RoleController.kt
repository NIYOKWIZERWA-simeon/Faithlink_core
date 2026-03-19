package com.faithlink.core.controller

import com.faithlink.core.entity.Role
import com.faithlink.core.service.RoleService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = ["*"])
class RoleController(
    private val roleService: RoleService
) {
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createRole(@Valid @RequestBody role: Role): ResponseEntity<Role> {
        val createdRole = roleService.createRole(role)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole)
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getRoleById(@PathVariable id: Long): ResponseEntity<Role> {
        return roleService.getRoleById(id)
            .map { role -> ResponseEntity.ok(role) }
            .orElse(ResponseEntity.notFound().build())
    }
    
    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    fun getRoleByName(@PathVariable name: String): ResponseEntity<Role> {
        return roleService.getRoleByName(name)
            .map { role -> ResponseEntity.ok(role) }
            .orElse(ResponseEntity.notFound().build())
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllRoles(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "id") sortBy: String,
        @RequestParam(defaultValue = "asc") sortDir: String
    ): ResponseEntity<Page<Role>> {
        val direction = if (sortDir.equals("desc", ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(direction, sortBy))
        val roles = roleService.getAllRoles(pageable)
        return ResponseEntity.ok(roles)
    }
    
    @GetMapping("/all")
    fun getAllRolesList(): ResponseEntity<List<Role>> {
        val roles = roleService.getAllRoles()
        return ResponseEntity.ok(roles)
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    fun searchRoles(@RequestParam name: String): ResponseEntity<List<Role>> {
        val roles = roleService.searchRolesByName(name)
        return ResponseEntity.ok(roles)
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateRole(
        @PathVariable id: Long,
        @Valid @RequestBody updatedRole: Role
    ): ResponseEntity<Role> {
        return roleService.updateRole(id, updatedRole)
            .map { role -> ResponseEntity.ok(role) }
            .orElse(ResponseEntity.notFound().build())
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteRole(@PathVariable id: Long): ResponseEntity<Void> {
        return if (roleService.deleteRole(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
