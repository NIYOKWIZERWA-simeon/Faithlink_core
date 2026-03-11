package com.faithlink.core

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("/")
    fun hello(): String {
        return "Faithlink Core is officially live! 🚀"
    }
}