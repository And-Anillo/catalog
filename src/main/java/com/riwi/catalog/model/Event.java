package com.riwi.catalog.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Event {
    private Long id;
    private String title;
    private String description;
}
