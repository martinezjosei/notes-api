package com.martinez.notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateNoteRequest(
        @NotBlank(message = "{note.title.required}")
        @Size(max = 10, message = "{note.title.max}")
        String title,

        @NotBlank(message = "{note.body.required}")
        @Size(max = 100, message = "{note.body.max}")
        String body
) {
    public UpdateNoteRequest {
        title = title == null ? null : title.trim();
        body  = body  == null ? null : body.trim();
    }
}
