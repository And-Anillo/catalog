package com.riwi.catalog.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Event {
    private String id;
    private String title;
    private String description;

    public Event(String title, String description) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
    }
}
