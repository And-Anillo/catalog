package com.riwi.catalog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events", uniqueConstraints = {
        // Constraint for 'nombre único de evento'
        @UniqueConstraint(columnNames = "name", name = "uk_event_name")
})
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Event name is mandatory")
    @Size(max = 150, message = "Name must be less than 150 characters")
    private String name;

    @NotBlank(message = "Category is mandatory")
    private String category;

    // Use @Future for validation (TASK 2)
    @Future(message = "Start date must be in the future")
    @Column(name = "start_date")
    private LocalDateTime startDate;

    // Many-to-One relationship with Venue
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    @NotNull(message = "Venue is mandatory")
    private VenueEntity venue;

    // Getters and Setters (omitted for brevity)

    // Default constructor
    public EventEntity() {}

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public VenueEntity getVenue() { return venue; }
    public void setVenue(VenueEntity venue) { this.venue = venue; }
}
