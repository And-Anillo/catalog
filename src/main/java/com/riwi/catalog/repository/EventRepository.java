package com.riwi.catalog.repository;

import com.riwi.catalog.model.Event;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventRepository {
    private List<Event> events = new ArrayList<>();

    public List<Event> findAll() {
        return new ArrayList<>(events);
    }

    public Optional<Event> findById(String id) {
        return events.stream()
                .filter(event -> event.getId().equals(id))
                .findFirst();
    }

    public Event save(Event event) {
        events.add(event);
        return event;
    }

    public void deleteById(String id) {
        events.removeIf(event -> event.getId().equals(id));
    }

    public boolean existsById(String id) {
        return events.stream().anyMatch(event -> event.getId().equals(id));
    }
}
