package com.faithlink.core.controller

import com.faithlink.core.entity.Event
import com.faithlink.core.service.EventService
import org.springframework.http.`HttpStatus`
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/events")
class EventController(private val eventService: EventService) {

    @GetMapping
    fun getAllEvents(): ResponseEntity<List<Event>> {
        return ResponseEntity.ok(eventService.getAllEvents())
    }

    @GetMapping("/{id}")
    fun getEventById(@PathVariable id: UUID): ResponseEntity<Event> {
        return eventService.getEventById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }
    
    @GetMapping("/church/{churchId}")
    fun getEventsByChurch(@PathVariable churchId: UUID): ResponseEntity<List<Event>> {
        return ResponseEntity.ok(eventService.getEventsByChurch(churchId))
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PASTOR')")
    fun createEvent(@RequestBody request: com.faithlink.core.dto.EventRequest): ResponseEntity<Event> {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEventFromDto(request))
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASTOR')")
    fun updateEvent(@PathVariable id: UUID, @RequestBody event: Event): ResponseEntity<Event> {
        return eventService.updateEvent(id, event)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASTOR')")
    fun deleteEvent(@PathVariable id: UUID): ResponseEntity<Void> {
        return if (eventService.deleteEvent(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
