package com.riwi.catalog.repository;
import com.riwi.catalog.model.Venue;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class VenueRepository {
    private List<Venue> venues = new ArrayList<>();

    public List<Venue> findAll() {
        return new ArrayList<>(venues);
    }

    public Optional<Venue> findById(String id) {
        return venues.stream()
                .filter(venue -> venue.getId().equals(id))
                .findFirst();
    }

    public Venue save(Venue venue) {
        venues.add(venue);
        return venue;
    }

    public void deleteById(String id) {
        venues.removeIf(venue -> venue.getId().equals(id));
    }

    public boolean existsById(String id) {
        return venues.stream().anyMatch(venue -> venue.getId().equals(id));
    }
}
