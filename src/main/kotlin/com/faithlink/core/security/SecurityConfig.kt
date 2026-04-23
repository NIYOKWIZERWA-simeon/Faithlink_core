package com.faithlink.core.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {
    
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { authorize ->
                authorize
                    // Public endpoints
                    .requestMatchers("/", "/index.html", "/login", "/login.html", "/dashboard", "/dashboard.html", "/test-login.html").permitAll()
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers("/error").permitAll()
                    
                    // Church & Role discovery (Public-ish)
                    .requestMatchers(HttpMethod.GET, "/api/churches/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/roles/**").permitAll()
                    
                    // Prayer Requests: Members can submit, but only authorized can see all
                    .requestMatchers(HttpMethod.POST, "/api/prayer-requests").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/prayer-requests/public").permitAll()
                    .requestMatchers("/api/prayer-requests/**").hasAnyRole("ADMIN", "PASTOR")
                    
                    // Sermons & Announcements: Publicly viewable
                    .requestMatchers(HttpMethod.GET, "/api/sermons/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/announcements/**").permitAll()
                    
                    // Finance & Donations
                    .requestMatchers(HttpMethod.POST, "/api/donations").authenticated()
                    .requestMatchers("/api/donations/my-donations").authenticated()
                    .requestMatchers("/api/donations/**").hasAnyRole("ADMIN", "TREASURER")
                    
                    // Sync & Offline-First
                    .requestMatchers("/api/sync/**").authenticated()
                    
                    // Group Management
                    .requestMatchers(HttpMethod.GET, "/api/groups/**").authenticated()
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    
                    // Default protection
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .headers { it.frameOptions { it.disable() } }
            .build()
    }
    
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
    
    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
    
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOriginPatterns = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true
        
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
