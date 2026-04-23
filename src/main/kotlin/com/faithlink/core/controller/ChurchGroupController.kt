package com.faithlink.core.controller

import com.faithlink.core.entity.ChurchGroup
import com.faithlink.core.service.ChurchGroupService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/groups")
class ChurchGroupController(private val churchGroupService: ChurchGroupService) {

    @GetMapping("/church/{churchId}")
    fun getGroupsByChurch(@PathVariable churchId: UUID): ResponseEntity<List<ChurchGroup>> {
        return ResponseEntity.ok(churchGroupService.getGroupsByChurch(churchId))
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createGroup(@RequestBody group: ChurchGroup): ResponseEntity<ChurchGroup> {
        return ResponseEntity.ok(churchGroupService.createGroup(group))
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteGroup(@PathVariable id: UUID): ResponseEntity<Void> {
        return if (churchGroupService.deleteGroup(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/{groupId}/enroll/{userId}")
    fun enrollMember(@PathVariable groupId: UUID, @PathVariable userId: UUID): ResponseEntity<Void> {
        return if (churchGroupService.enrollMember(groupId, userId)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.badRequest().build()
        }
    }
}
