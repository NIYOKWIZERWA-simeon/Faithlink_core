package com.faithlink.core.controller

import com.faithlink.core.entity.User
import com.faithlink.core.service.UserService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    fun createUser(@Valid @RequestBody user: User): ResponseEntity<User> {
        val createdUser = userService.createUser(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER') or @userService.isOwner(#id, authentication.name)")
    fun getUserById(@PathVariable id: UUID): ResponseEntity<User> {
        return userService.getUserById(id)
            .map { user -> ResponseEntity.ok(user) }
            .orElse(ResponseEntity.notFound().build())
    }
    
    @GetMapping("/church/{churchId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASTOR')")
    fun getUsersByChurch(@PathVariable churchId: UUID): ResponseEntity<List<User>> {
        val users = userService.getUsersByChurch(churchId)
        return ResponseEntity.ok(users)
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER') or authentication.name == #email")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<User> {
        return userService.getUserByEmail(email)
            .map { user -> ResponseEntity.ok(user) }
            .orElse(ResponseEntity.notFound().build())
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    fun getAllUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "id") sortBy: String,
        @RequestParam(defaultValue = "asc") sortDir: String
    ): ResponseEntity<Page<User>> {
        val direction = if (sortDir.equals("desc", ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(direction, sortBy))
        val users = userService.getAllUsers(pageable)
        return ResponseEntity.ok(users)
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER')")
    fun getActiveUsers(): ResponseEntity<List<User>> {
        val users = userService.getActiveUsers()
        return ResponseEntity.ok(users)
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER') or @userService.isOwner(#id, authentication.name)")
    fun updateUser(
        @PathVariable id: UUID,
        @Valid @RequestBody updatedUser: User
    ): ResponseEntity<User> {
        return userService.updateUser(id, updatedUser)
            .map { user -> ResponseEntity.ok(user) }
            .orElse(ResponseEntity.notFound().build())
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteUser(@PathVariable id: UUID): ResponseEntity<Void> {
        return if (userService.deleteUser(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    fun softDeleteUser(@PathVariable id: UUID): ResponseEntity<Void> {
        return if (userService.softDeleteUser(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
