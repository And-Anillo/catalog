package com.riwi.catalog.service;

import com.riwi.catalog.dto.EventDTO;
import com.riwi.catalog.model.Event;
import com.riwi.catalog.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(String id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(EventDTO eventDTO) {
        Event event = new Event(eventDTO.getTitle(), eventDTO.getDescription());
        return eventRepository.save(event);
    }

    public Optional<Event> updateEvent(String id, EventDTO eventDTO) {
        Optional<Event> existingEvent = eventRepository.findById(id);
        if (existingEvent.isPresent()) {
            Event event = existingEvent.get();
            event.setTitle(eventDTO.getTitle());
            event.setDescription(eventDTO.getDescription());
            return Optional.of(eventRepository.save(event));
        }
        return Optional.empty();
    }

    public boolean deleteEvent(String id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
