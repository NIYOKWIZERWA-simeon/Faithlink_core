package com.faithlink.core.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil {
    
    @Value("\${jwt.secret:faithlink-secret-key-for-jwt-token-generation}")
    private lateinit var secret: String
    
    @Value("\${jwt.expiration:86400000}") // 24 hours in milliseconds
    private val expiration: Long = 86400000
    
    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray())
    }
    
    fun generateToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = emptyMap()
        return createToken(claims, userDetails.username)
    }
    
    fun generateToken(email: String, roles: Set<String>): String {
        val claims: Map<String, Any> = mapOf(
            "roles" to roles
        )
        return createToken(claims, email)
    }
    
    private fun createToken(claims: Map<String, Any>, subject: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)
        
        return Jwts.builder()
            .claims(claims)
            .subject(subject)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact()
    }
    
    fun extractUsername(token: String): String {
        return extractClaim(token) { obj: Claims -> obj.subject }
    }
    
    fun extractExpiration(token: String): Date {
        return extractClaim(token) { obj: Claims -> obj.expiration }
    }
    
    fun extractRoles(token: String): Set<String> {
        val claims = extractAllClaims(token)
        @Suppress("UNCHECKED_CAST")
        return (claims["roles"] as? List<String>)?.toSet() ?: emptySet()
    }
    
    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }
    
    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }
    
    fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }
    
    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }
    
    fun validateToken(token: String): Boolean {
        return try {
            extractUsername(token).isNotEmpty() && !isTokenExpired(token)
        } catch (e: Exception) {
            false
        }
    }
    
    fun refreshToken(token: String): String? {
        return try {
            val claims = extractAllClaims(token)
            val username = claims.subject
            @Suppress("UNCHECKED_CAST")
            val roles = (claims["roles"] as? List<String>)?.toSet() ?: emptySet()
            generateToken(username, roles)
        } catch (e: Exception) {
            null
        }
    }
}
