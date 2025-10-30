package com.riwi.catalog.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Venue {
    private String id;
    private String name;
    private String location;

    public Venue(String name, String location) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.location = location;
    }
}
