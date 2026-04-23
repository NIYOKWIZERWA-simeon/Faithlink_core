package com.faithlink.core.config

import com.faithlink.core.entity.*
import com.faithlink.core.repository.*
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Component
class DataSeeder(
    private val churchRepository: ChurchRepository,
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository,
    private val churchGroupRepository: ChurchGroupRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    @Transactional
    override fun run(vararg args: String?) {
        // Only seed if roles don't exist
        if (roleRepository.count() > 0) {
            println("Database already has data. Skipping seeding.")
            return
        }

        // 1. Seed Roles
        val adminRole = roleRepository.save(Role(name = "ROLE_ADMIN"))
        val pastorRole = roleRepository.save(Role(name = "ROLE_PASTOR"))
        val memberRole = roleRepository.save(Role(name = "ROLE_MEMBER"))
        val treasurerRole = roleRepository.save(Role(name = "ROLE_TREASURER"))

        // 2. Seed Main Church
        val mainChurch = churchRepository.save(Church(
            name = "FaithLink Central Church",
            address = "123 Grace Blvd, Spirit City",
            email = "central@faithlink.org",
            isActive = true
        ))

        // 3. Seed Users
        val adminUser = userRepository.save(User(
            firstName = "System",
            lastName = "Administrator",
            email = "admin@faithlink.org",
            password = passwordEncoder.encode("admin123"),
            church = mainChurch,
            roles = mutableSetOf(adminRole)
        ))

        val pastorUser = userRepository.save(User(
            firstName = "John",
            lastName = "Pastor",
            email = "pastor@faithlink.org",
            password = passwordEncoder.encode("pastor123"),
            church = mainChurch,
            roles = mutableSetOf(pastorRole)
        ))

        val memberUser = userRepository.save(User(
            firstName = "Faith",
            lastName = "Member",
            email = "member@faithlink.org",
            password = passwordEncoder.encode("member123"),
            church = mainChurch,
            roles = mutableSetOf(memberRole)
        ))

        val treasurerUser = userRepository.save(User(
            firstName = "Grace",
            lastName = "Treasurer",
            email = "treasurer@faithlink.org",
            password = passwordEncoder.encode("treasurer123"),
            church = mainChurch,
            roles = mutableSetOf(treasurerRole)
        ))

        // 4. Seed Groups
        churchGroupRepository.save(ChurchGroup(
            name = "Youth For Christ",
            description = "Empowering the next generation",
            church = mainChurch,
            leader = pastorUser
        ))

        churchGroupRepository.save(ChurchGroup(
            name = "Celestial Choir",
            description = "Lifting voices in praise",
            church = mainChurch,
            leader = memberUser
        ))

        println("Data seeding completed successfully. Users ready: admin@faithlink.org (admin123), pastor@faithlink.org (pastor123), member@faithlink.org (member123)")
    }
}
