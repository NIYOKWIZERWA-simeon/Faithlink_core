package com.faithlink.core.controller

import com.faithlink.core.dto.AnnouncementRequest
import com.faithlink.core.dto.AnnouncementResponse
import com.faithlink.core.service.AnnouncementService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/announcements")
class AnnouncementController(
    private val announcementService: AnnouncementService
) {

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PASTOR')")
    fun createAnnouncement(@Valid @RequestBody request: AnnouncementRequest): ResponseEntity<AnnouncementResponse> {
        val created = announcementService.createAnnouncement(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping("/church/{churchId}")
    fun getAnnouncementsByChurch(@PathVariable churchId: UUID): ResponseEntity<List<AnnouncementResponse>> {
        val announcements = announcementService.getAnnouncementsByChurch(churchId)
        return ResponseEntity.ok(announcements)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASTOR')")
    fun deleteAnnouncement(@PathVariable id: UUID): ResponseEntity<Void> {
        announcementService.deleteAnnouncement(id)
        return ResponseEntity.noContent().build()
    }
}
