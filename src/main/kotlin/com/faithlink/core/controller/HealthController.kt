package com.faithlink.core.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/public/health")
class HealthController {
    
    @GetMapping
    fun healthCheck(): Map<String, String> {
        return mapOf("status" to "UP", "message" to "System Online")
    }
}
