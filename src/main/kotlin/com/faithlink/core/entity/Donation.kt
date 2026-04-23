package com.faithlink.core.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "donations")
data class Donation(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id", nullable = false)
    var church: Church,
    
    @Column(nullable = false, precision = 19, scale = 4)
    var amount: BigDecimal,
    
    @Column(name = "fund_type", nullable = false, length = 50)
    var fundType: String, // e.g., Tithe, Building Fund, Mission
    
    @Column(name = "payment_method", nullable = false, length = 50)
    var paymentMethod: String, // e.g., Mobile Money, Bank Transfer, Cash
    
    @Column(length = 500)
    var notes: String? = null,
    
    @Column(name = "transaction_id", unique = true)
    var transactionId: String? = null,
    
    @Column(name = "donation_date", nullable = false)
    val donationDate: LocalDateTime = LocalDateTime.now(),
    
    @Column(name = "is_verified", nullable = false)
    var isVerified: Boolean = false
)
