package com.faithlink.core.controller

import com.faithlink.core.entity.Sermon
import com.faithlink.core.service.SermonService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/sermons")
class SermonController(private val sermonService: SermonService) {

    @GetMapping("/church/{churchId}")
    fun getSermonsByChurch(@PathVariable churchId: UUID): ResponseEntity<List<Sermon>> {
        return ResponseEntity.ok(sermonService.getSermonsByChurch(churchId))
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PASTOR')")
    fun createSermon(@RequestBody request: com.faithlink.core.dto.SermonCreateRequest): ResponseEntity<Sermon> {
        return ResponseEntity.ok(sermonService.createSermonFromDto(request))
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASTOR')")
    fun deleteSermon(@PathVariable id: UUID): ResponseEntity<Void> {
        return if (sermonService.deleteSermon(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
