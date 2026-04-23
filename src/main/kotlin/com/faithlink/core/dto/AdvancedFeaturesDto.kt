package com.faithlink.core.dto

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class GroupSummaryDto(
    val id: UUID,
    val name: String,
    val description: String?,
    val leaderId: UUID?,
    val isActive: Boolean
)

data class DonationRequest(
    val amount: BigDecimal,
    val fundType: String,
    val paymentMethod: String,
    val notes: String? = null,
    val churchId: UUID,
    val userId: UUID
)

data class DonationResponse(
    val id: UUID,
    val userId: UUID,
    val userName: String,
    val amount: BigDecimal,
    val fundType: String,
    val paymentMethod: String,
    val notes: String?,
    val donationDate: LocalDateTime,
    val isVerified: Boolean,
    val churchId: UUID
)

data class EventRequest(
    val title: String,
    val description: String? = null,
    val location: String,
    val startTime: String, // String to handle browser format
    val endTime: String,
    val churchId: UUID
)

data class PrayerRequestDto(
    val subject: String,
    val content: String,
    val churchId: UUID,
    val userId: UUID,
    val isAnonymous: Boolean = false
)

data class EventResponse(
    val id: UUID,
    val title: String,
    val description: String?,
    val location: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val churchId: UUID
)
