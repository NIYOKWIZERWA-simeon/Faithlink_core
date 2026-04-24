package com.faithlink.core.service

import com.faithlink.core.entity.Event
import com.faithlink.core.repository.EventRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class EventService(
    private val eventRepository: EventRepository,
    private val churchRepository: com.faithlink.core.repository.ChurchRepository
) {
    fun createEventFromDto(dto: com.faithlink.core.dto.EventRequest): Event {
        val church = churchRepository.findById(dto.churchId).orElseThrow { RuntimeException("Church not found") }
        
        val formatter = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
        
        val event = Event(
            title = dto.title,
            description = dto.description,
            location = dto.location,
            startTime = java.time.LocalDateTime.parse(dto.startTime, formatter),
            endTime = java.time.LocalDateTime.parse(dto.endTime, formatter),
            church = church
        )
        return eventRepository.save(event)
    }

    @Transactional(readOnly = true)
    fun getAllEvents(): List<Event> {
        return eventRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun getEventById(id: UUID): Optional<Event> {
        return eventRepository.findById(id)
    }
    
    @Transactional(readOnly = true)
    fun getEventsByChurch(churchId: UUID): List<Event> {
        return eventRepository.findByChurchId(churchId)
    }

    @Transactional
    fun createEvent(event: Event): Event {
        return eventRepository.save(event)
    }

    @Transactional
    fun updateEvent(id: UUID, updatedEvent: Event): Optional<Event> {
        return eventRepository.findById(id).map { event ->
            event.title = updatedEvent.title
            event.description = updatedEvent.description
            event.location = updatedEvent.location
            event.startTime = updatedEvent.startTime
            event.endTime = updatedEvent.endTime
            event.church = updatedEvent.church ?: event.church
            
            eventRepository.save(event)
        }
    }

    @Transactional
    fun deleteEvent(id: UUID): Boolean {
        return if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}
