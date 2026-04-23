package com.faithlink.core.controller

import com.faithlink.core.entity.Group
import com.faithlink.core.service.GroupService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/groups")
class GroupController(private val groupService: GroupService) {

    @GetMapping("/church/{churchId}")
    fun getGroupsByChurch(@PathVariable churchId: UUID): ResponseEntity<List<Group>> {
        return ResponseEntity.ok(groupService.getGroupsByChurch(churchId))
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createGroup(@RequestBody group: Group): ResponseEntity<Group> {
        return ResponseEntity.ok(groupService.createGroup(group))
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteGroup(@PathVariable id: UUID): ResponseEntity<Void> {
        return if (groupService.deleteGroup(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/{groupId}/enroll/{userId}")
    fun enrollMember(@PathVariable groupId: UUID, @PathVariable userId: UUID): ResponseEntity<Void> {
        return if (groupService.enrollMember(groupId, userId)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.badRequest().build()
        }
    }
}
