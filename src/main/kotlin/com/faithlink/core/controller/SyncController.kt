package com.faithlink.core.controller

import com.faithlink.core.service.SyncService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/sync")
class SyncController(private val syncService: SyncService) {

    @GetMapping("/bootstrap/{churchId}")
    fun bootstrap(@PathVariable churchId: UUID): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(syncService.getBootstrapData(churchId))
    }
}
