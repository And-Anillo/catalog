package com.riwi.catalog.controller;

import com.riwi.catalog.model.EventEntity;
import com.riwi.catalog.service.EventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // TASK 3: GET /events?page=&size=&sort= con Pageable y Filtros
    @GetMapping
    public ResponseEntity<Page<EventEntity>> getAllEvents(
            // Spring Data JPA automatically resolves these parameters into a Pageable object
            Pageable pageable,

            // Optional Filters
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate
    ) {
        Page<EventEntity> events = eventService.findAllEvents(city, category, startDate, pageable);
        return ResponseEntity.ok(events);
    }

    // TASK 2: Apply @Valid
    @PostMapping
    public ResponseEntity<EventEntity> createEvent(@Valid @RequestBody EventEntity event) {
        EventEntity newEvent = eventService.createEvent(event);
        return new ResponseEntity<>(newEvent, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventEntity> getEventById(@PathVariable Long id) {
        EventEntity event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    // TASK 2: Apply @Valid
    @PutMapping("/{id}")
    public ResponseEntity<EventEntity> updateEvent(@PathVariable Long id, @Valid @RequestBody EventEntity eventDetails) {
        EventEntity updatedEvent = eventService.updateEvent(id, eventDetails);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}
