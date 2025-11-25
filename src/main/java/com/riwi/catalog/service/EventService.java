package com.riwi.catalog.service;

import com.riwi.catalog.exeption.ResourceNotFoundException;
import com.riwi.catalog.exeption.ValidationException;
import com.riwi.catalog.model.EventEntity;
import com.riwi.catalog.model.VenueEntity;
import com.riwi.catalog.repository.EventRepository;
import com.riwi.catalog.repository.VenueRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    public EventService(EventRepository eventRepository, VenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    // Helper method for venue existence check
    private VenueEntity findVenueById(Long venueId) {
        return venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID: " + venueId));
    }

    // Helper method for name uniqueness check (TASK 2)
    private void checkNameUniqueness(String name, Long currentId) {
        // If currentId is null, we are checking for a new creation.
        Optional<EventEntity> existingEvent = eventRepository.findByNameAndIdNot(name, currentId == null ? -1L : currentId);

        if (existingEvent.isPresent()) {
            throw new ValidationException("Event name '" + name + "' already exists.");
        }
    }

    // CRUD: Create (TASK 1 & 2)
    @Transactional
    public EventEntity createEvent(EventEntity event) {
        // Ensure Venue exists
        VenueEntity venue = findVenueById(event.getVenue().getId());
        event.setVenue(venue);

        // Check for name duplicates (TASK 2)
        checkNameUniqueness(event.getName(), null);

        return eventRepository.save(event);
    }

    // CRUD: Read one
    public EventEntity getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));
    }

    // CRUD: Read all with Pagination and optional Filters (TASK 3)
    // This is a simplified version of filtering for illustration.
    public Page<EventEntity> findAllEvents(
            String city,
            String category,
            LocalDateTime startDate,
            Pageable pageable) {

        // Use a more specific repository method if all filters are present.
        // For production, you'd use Spring Data JPA Specifications or QueryDSL.
        if (city != null && category != null && startDate != null) {
            return eventRepository.findAllByVenueCityAndCategoryAndStartDateAfter(city, category, startDate, pageable);
        }

        // Default to findAll with Pageable
        return eventRepository.findAll(pageable);
    }

    // CRUD: Update (TASK 1 & 2)
    @Transactional
    public EventEntity updateEvent(Long id, EventEntity eventDetails) {
        EventEntity event = getEventById(id); // Throws 404 if not found

        // Check for name duplicates, excluding the current event's ID (TASK 2)
        checkNameUniqueness(eventDetails.getName(), id);

        // Update fields
        event.setName(eventDetails.getName());
        event.setCategory(eventDetails.getCategory());
        event.setStartDate(eventDetails.getStartDate());

        // Update Venue reference
        VenueEntity venue = findVenueById(eventDetails.getVenue().getId());
        event.setVenue(venue);

        return eventRepository.save(event);
    }

    // CRUD: Delete
    public void deleteEvent(Long id) {
        EventEntity event = getEventById(id); // Throws 404 if not found
        eventRepository.delete(event);
    }
}
