package com.riwi.catalog.service;

import com.riwi.catalog.exception.ResourceNotFoundException;
import com.riwi.catalog.exception.ValidationException;
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
}
