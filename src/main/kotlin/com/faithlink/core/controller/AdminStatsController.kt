package com.faithlink.core.controller

import com.faithlink.core.service.UserService
import com.faithlink.core.service.DonationService
import com.faithlink.core.service.EventService
import com.faithlink.core.service.ChurchGroupService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID
import java.math.BigDecimal

@RestController
@RequestMapping("/api/admin/stats")
@PreAuthorize("hasAnyRole('ADMIN', 'PASTOR', 'TREASURER')")
class AdminStatsController(
    private val userService: UserService,
    private val donationService: DonationService,
    private val eventService: EventService,
    private val churchGroupService: ChurchGroupService
) {

    @GetMapping("/church/{churchId}")
    fun getChurchStats(@PathVariable churchId: UUID): ResponseEntity<Map<String, Any>> {
        val stats = mutableMapOf<String, Any>()
        
        val users = userService.getUsersByChurch(churchId)
        stats["userCount"] = users.size
        
        val donations = donationService.getDonationsByChurch(churchId)
        stats["donationCount"] = donations.size
        stats["totalDonations"] = donations.map { it.amount }.fold(BigDecimal.ZERO) { acc, amount -> acc.add(amount) }
        
        val events = eventService.getEventsByChurch(churchId)
        stats["eventCount"] = events.size
        
        val groups = churchGroupService.getGroupsByChurch(churchId)
        stats["groupCount"] = groups.size
        
        return ResponseEntity.ok(stats)
    }

    @GetMapping("/total/users")
    fun getUserCount(): ResponseEntity<Long> {
        return ResponseEntity.ok(userService.getActiveUsers().size.toLong())
    }
}
