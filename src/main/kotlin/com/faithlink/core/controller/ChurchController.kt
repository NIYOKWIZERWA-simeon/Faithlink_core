package com.faithlink.core.controller

import com.faithlink.core.entity.Church
import com.faithlink.core.service.ChurchService
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
@RequestMapping("/api/churches")
@CrossOrigin(origins = ["*"])
class ChurchController(
    private val churchService: ChurchService
) {
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CHURCH_MANAGER')")
    fun createChurch(@Valid @RequestBody church: Church): ResponseEntity<Church> {
        val createdChurch = churchService.createChurch(church)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChurch)
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CHURCH_MANAGER')")
    fun getChurchById(@PathVariable id: Long): ResponseEntity<Church> {
        return churchService.getChurchById(id)
            .map { church -> ResponseEntity.ok(church) }
            .orElse(ResponseEntity.notFound().build())
    }
    
    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CHURCH_MANAGER')")
    fun getChurchByName(@PathVariable name: String): ResponseEntity<Church> {
        return churchService.getChurchByName(name)
            .map { church -> ResponseEntity.ok(church) }
            .orElse(ResponseEntity.notFound().build())
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CHURCH_MANAGER')")
    fun getAllChurches(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "id") sortBy: String,
        @RequestParam(defaultValue = "asc") sortDir: String
    ): ResponseEntity<Page<Church>> {
        val direction = if (sortDir.equals("desc", ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(direction, sortBy))
        val churches = churchService.getAllChurches(pageable)
        return ResponseEntity.ok(churches)
    }
    
    @GetMapping("/active")
    fun getActiveChurches(): ResponseEntity<List<Church>> {
        val churches = churchService.getActiveChurches()
        return ResponseEntity.ok(churches)
    }
    
    @GetMapping("/search")
    fun searchChurches(@RequestParam name: String): ResponseEntity<List<Church>> {
        val churches = churchService.searchChurchesByName(name)
        return ResponseEntity.ok(churches)
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CHURCH_MANAGER')")
    fun updateChurch(
        @PathVariable id: Long,
        @Valid @RequestBody updatedChurch: Church
    ): ResponseEntity<Church> {
        return churchService.updateChurch(id, updatedChurch)
            .map { church -> ResponseEntity.ok(church) }
            .orElse(ResponseEntity.notFound().build())
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteChurch(@PathVariable id: Long): ResponseEntity<Void> {
        return if (churchService.deleteChurch(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    fun softDeleteChurch(@PathVariable id: Long): ResponseEntity<Void> {
        return if (churchService.softDeleteChurch(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping("/stats/count")
    fun getActiveChurchesCount(): ResponseEntity<Map<String, Long>> {
        val count = churchService.countActiveChurches()
        return ResponseEntity.ok(mapOf("activeChurches" to count))
    }
}
