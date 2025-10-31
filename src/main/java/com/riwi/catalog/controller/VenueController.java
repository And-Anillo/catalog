package com.riwi.catalog.controller;
import com.riwi.catalog.dto.VenueDTO;
import com.riwi.catalog.model.Venue;
import com.riwi.catalog.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/venues")
public class VenueController {
    @Autowired
    private VenueService venueService;

    @GetMapping
    public ResponseEntity<List<Venue>> getAllVenues() {
        List<Venue> venues = venueService.getAllVenues();
        return ResponseEntity.ok(venues);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venue> getVenueById(@PathVariable String id) {
        Optional<Venue> venue = venueService.getVenueById(id);
        return venue.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Venue> createVenue(@Validated @RequestBody VenueDTO venueDTO) {
        Venue createdVenue = venueService.createVenue(venueDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVenue);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venue> updateVenue(@PathVariable String id, @Validated @RequestBody VenueDTO venueDTO) {
        Optional<Venue> updatedVenue = venueService.updateVenue(id, venueDTO);
        return updatedVenue.map(venue -> ResponseEntity.ok(venue))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable String id) {
        boolean deleted = venueService.deleteVenue(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
