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
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/roles/all").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/roles").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/churches").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/churches/active").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/churches/search").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/churches/stats/count").permitAll()
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers("/error").permitAll()
                    // Admin endpoints
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    // Protected endpoints
                    .requestMatchers(HttpMethod.POST, "/api/users").hasAnyRole("ADMIN", "USER_MANAGER")
                    .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("ADMIN", "USER_MANAGER")
                    .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/api/users/**/deactivate").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/roles").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/roles/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/roles/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/churches").hasAnyRole("ADMIN", "CHURCH_MANAGER")
                    .requestMatchers(HttpMethod.PUT, "/api/churches/**").hasAnyRole("ADMIN", "CHURCH_MANAGER")
                    .requestMatchers(HttpMethod.DELETE, "/api/churches/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/api/churches/**/deactivate").hasRole("ADMIN")
                    // User profile endpoints (authenticated users)
                    .requestMatchers(HttpMethod.GET, "/api/users/profile").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/api/users/profile").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/api/users/password").authenticated()
                    // All other requests need authentication
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .headers { it.frameOptions { it.disable() } } // For H2 console
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
