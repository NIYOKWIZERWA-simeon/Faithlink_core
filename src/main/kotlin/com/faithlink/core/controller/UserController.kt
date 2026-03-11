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
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = ["*"])
class UserController(
    private val userService: UserService
) {
    
    @PostMapping
    fun createUser(@Valid @RequestBody user: User): ResponseEntity<User> {
        val createdUser = userService.createUser(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
    }
    
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> {
        return userService.getUserById(id)
            .map { user -> ResponseEntity.ok(user) }
            .orElse(ResponseEntity.notFound().build())
    }
    
    @GetMapping("/email/{email}")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<User> {
        return userService.getUserByEmail(email)
            .map { user -> ResponseEntity.ok(user) }
            .orElse(ResponseEntity.notFound().build())
    }
    
    @GetMapping
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
    fun getActiveUsers(): ResponseEntity<List<User>> {
        val users = userService.getActiveUsers()
        return ResponseEntity.ok(users)
    }
    
    @GetMapping("/search")
    fun searchUsers(
        @RequestParam(required = false) firstName: String?,
        @RequestParam(required = false) lastName: String?
    ): ResponseEntity<List<User>> {
        val users = userService.searchUsersByName(firstName, lastName)
        return ResponseEntity.ok(users)
    }
    
    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody updatedUser: User
    ): ResponseEntity<User> {
        return userService.updateUser(id, updatedUser)
            .map { user -> ResponseEntity.ok(user) }
            .orElse(ResponseEntity.notFound().build())
    }
    
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        return if (userService.deleteUser(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @PatchMapping("/{id}/deactivate")
    fun softDeleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        return if (userService.softDeleteUser(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping("/stats/count")
    fun getActiveUsersCount(): ResponseEntity<Map<String, Long>> {
        val count = userService.countActiveUsers()
        return ResponseEntity.ok(mapOf("activeUsers" to count))
    }
}
