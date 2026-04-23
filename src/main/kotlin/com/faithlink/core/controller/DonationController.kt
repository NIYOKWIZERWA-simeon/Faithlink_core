package com.faithlink.core.controller

import com.faithlink.core.entity.Donation
import com.faithlink.core.service.DonationService
import com.faithlink.core.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.UUID

@RestController
@RequestMapping("/api/donations")
class DonationController(
    private val donationService: DonationService,
    private val userRepository: UserRepository
) {

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TREASURER', 'PASTOR')")
    fun getAllDonations(): ResponseEntity<List<Donation>> {
        return ResponseEntity.ok(donationService.getAllDonations())
    }

    @PostMapping
    fun createDonation(@RequestBody request: com.faithlink.core.dto.DonationRequest): ResponseEntity<Donation> {
        return ResponseEntity.ok(donationService.createDonationFromDto(request))
    }

    @GetMapping("/my-donations")
    fun getMyDonations(principal: Principal): ResponseEntity<List<Donation>> {
        val user = userRepository.findByEmail(principal.name)
            .orElseThrow { RuntimeException("User not found") }
        return ResponseEntity.ok(donationService.getDonationsByUserId(user.id!!))
    }
    
    @GetMapping("/church/{churchId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TREASURER', 'PASTOR')")
    fun getDonationsByChurch(@PathVariable churchId: UUID): ResponseEntity<List<Donation>> {
        return ResponseEntity.ok(donationService.getDonationsByChurch(churchId))
    }

    @PatchMapping("/{id}/verify")
    @PreAuthorize("hasAnyRole('ADMIN', 'TREASURER', 'PASTOR')")
    fun verifyDonation(@PathVariable id: UUID): ResponseEntity<Donation> {
        return donationService.verifyDonation(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }
}
