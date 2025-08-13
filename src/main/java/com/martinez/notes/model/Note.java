package com.martinez.notes.model;

import java.time.LocalDateTime;

public record Note(
        Long id,
        String title,
        String body,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
