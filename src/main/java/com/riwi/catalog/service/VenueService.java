package com.riwi.catalog.service;
import com.riwi.catalog.dto.VenueDTO;
import com.riwi.catalog.model.Venue;
import com.riwi.catalog.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class VenueService {
    @Autowired
    private VenueRepository venueRepository;

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Optional<Venue> getVenueById(String id) {
        return venueRepository.findById(id);
    }

    public Venue createVenue(VenueDTO venueDTO) {
        Venue venue = new Venue(venueDTO.getName(), venueDTO.getLocation());
        return venueRepository.save(venue);
    }

    public Optional<Venue> updateVenue(String id, VenueDTO venueDTO) {
        Optional<Venue> existingVenue = venueRepository.findById(id);
        if (existingVenue.isPresent()) {
            Venue venue = existingVenue.get();
            venue.setName(venueDTO.getName());
            venue.setLocation(venueDTO.getLocation());
            return Optional.of(venueRepository.save(venue));
        }
        return Optional.empty();
    }

    public boolean deleteVenue(String id) {
        if (venueRepository.existsById(id)) {
            venueRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
