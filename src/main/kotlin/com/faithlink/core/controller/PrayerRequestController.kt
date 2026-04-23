package com.faithlink.core.controller

import com.faithlink.core.entity.PrayerRequest
import com.faithlink.core.service.PrayerRequestService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.UUID

@RestController
@RequestMapping("/api/prayer-requests")
class PrayerRequestController(
    private val prayerRequestService: PrayerRequestService,
    private val userService: com.faithlink.core.service.UserService
) {
    @PostMapping
    fun submitRequest(
        @RequestBody dto: com.faithlink.core.dto.PrayerRequestDto
    ): ResponseEntity<PrayerRequest> {
        val request = prayerRequestService.submitRequest(dto.userId, dto.churchId, dto.subject, dto.content, dto.isAnonymous)
        return ResponseEntity.ok(request)
    }

    @GetMapping("/church/{churchId}")
    fun getRequestsByChurch(@PathVariable churchId: UUID): ResponseEntity<List<PrayerRequest>> {
        return ResponseEntity.ok(prayerRequestService.getRequestsByChurch(churchId))
    }

    @GetMapping("/church/{churchId}/public")
    fun getPublicRequests(@PathVariable churchId: UUID): ResponseEntity<List<PrayerRequest>> {
        return ResponseEntity.ok(prayerRequestService.getPublicRequestsByChurch(churchId))
    }

    @PostMapping("/{requestId}/respond")
    fun respondToRequest(@PathVariable requestId: UUID, @RequestBody body: Map<String, String>): ResponseEntity<PrayerRequest> {
        val response = body["response"] ?: throw IllegalArgumentException("Response is required")
        return ResponseEntity.ok(prayerRequestService.respondToRequest(requestId, response))
    }
}
