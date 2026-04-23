package com.faithlink.core.service

import com.faithlink.core.entity.Donation
import com.faithlink.core.repository.DonationRepository
import com.faithlink.core.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import java.util.Optional

@Service
class DonationService(
    private val donationRepository: DonationRepository,
    private val userRepository: UserRepository,
    private val churchRepository: com.faithlink.core.repository.ChurchRepository
) {
    @Transactional
    fun createDonationFromDto(dto: com.faithlink.core.dto.DonationRequest): Donation {
        val user = userRepository.findById(dto.userId).orElseThrow { RuntimeException("User not found") }
        val church = churchRepository.findById(dto.churchId).orElseThrow { RuntimeException("Church not found") }
        
        val donation = Donation(
            user = user,
            church = church,
            amount = dto.amount,
            fundType = dto.fundType,
            paymentMethod = dto.paymentMethod,
            notes = dto.notes,
            transactionId = UUID.randomUUID().toString(),
            isVerified = false
        )
        return donationRepository.save(donation)
    }

    @Transactional(readOnly = true)
    fun getAllDonations(): List<Donation> {
        return donationRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun getDonationsByUserId(userId: UUID): List<Donation> {
        return donationRepository.findByUserId(userId)
    }
    
    @Transactional(readOnly = true)
    fun getDonationsByChurch(churchId: UUID): List<Donation> {
        return donationRepository.findByChurchId(churchId)
    }

    @Transactional
    fun createDonation(donation: Donation): Donation {
        if (donation.transactionId == null) {
            donation.transactionId = UUID.randomUUID().toString()
        }
        return donationRepository.save(donation)
    }

    @Transactional
    fun verifyDonation(donationId: UUID): Optional<Donation> {
        return donationRepository.findById(donationId).map { donation ->
            donation.isVerified = true
            donationRepository.save(donation)
        }
    }
}
