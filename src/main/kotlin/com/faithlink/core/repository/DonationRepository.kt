package com.faithlink.core.repository

import com.faithlink.core.entity.Donation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DonationRepository : JpaRepository<Donation, UUID> {
    fun findByUserId(userId: UUID): List<Donation>
    fun findByFundType(fundType: String): List<Donation>
    fun findByIsVerified(isVerified: Boolean): List<Donation>
    fun findByTransactionId(transactionId: String): Donation?
    fun findByChurchId(churchId: UUID): List<Donation>
}
