package com.martinez.notes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinez.notes.dto.CreateNoteRequest;
import com.martinez.notes.dto.UpdateNoteRequest;
import com.martinez.notes.exception.GlobalExceptionHandler;
import com.martinez.notes.exception.NoteNotFoundException;
import com.martinez.notes.model.Note;
import com.martinez.notes.service.NoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NoteController.class)
@Import(GlobalExceptionHandler.class)
class NoteControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean NoteService noteService;

    private Note sampleNote() {
        var now = LocalDateTime.now();
        return new Note(1L, "Title", "Body", now, now);
    }

    @Test
    @DisplayName("POST /notes -> 201 Created")
    void createReturns201() throws Exception {
        var req = new CreateNoteRequest("Title", "Body");
        Mockito.when(noteService.create(any(CreateNoteRequest.class))).thenReturn(sampleNote());

        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Title")))
                .andExpect(jsonPath("$.body", is("Body")));
    }

    @Test
    @DisplayName("GET /notes -> 200 OK with list")
    void allReturns200() throws Exception {
        Mockito.when(noteService.all()).thenReturn(List.of(sampleNote()));

        mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    @DisplayName("GET /notes/{id} -> 200 OK")
    void oneReturns200() throws Exception {
        Mockito.when(noteService.one(1L)).thenReturn(sampleNote());

        mockMvc.perform(get("/notes/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @DisplayName("GET /notes/{id} -> 404 when not found")
    void oneReturns404WhenMissing() throws Exception {
        Mockito.when(noteService.one(999L)).thenThrow(new NoteNotFoundException(999L));

        mockMvc.perform(get("/notes/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsStringIgnoringCase("not")))
                .andExpect(jsonPath("$.message", containsString("999")));
    }

    @Test
    @DisplayName("PUT /notes/{id} -> 200 OK")
    void updateReturns200() throws Exception {
        var req = new UpdateNoteRequest("New", "Updated");
        var now = LocalDateTime.now();
        var updated = new Note(1L, "New", "Updated", now.minusMinutes(1), now);

        Mockito.when(noteService.update(eq(1L), any(UpdateNoteRequest.class))).thenReturn(updated);

        mockMvc.perform(put("/notes/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New")))
                .andExpect(jsonPath("$.body", is("Updated")));
    }

    @Test
    @DisplayName("DELETE /notes/{id} -> 204 No Content")
    void deleteReturns204() throws Exception {
        Mockito.doNothing().when(noteService).delete(1L);

        mockMvc.perform(delete("/notes/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /notes/{id} -> 404 when not found")
    void deleteReturns404WhenMissing() throws Exception {
        Mockito.doThrow(new NoteNotFoundException(123L)).when(noteService).delete(123L);

        mockMvc.perform(delete("/notes/{id}", 123))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("123")));
    }


    @Test
    @DisplayName("POST /notes -> 400 Bad Request for invalid body (missing title)")
    void createReturns400OnValidationError() throws Exception {
        var invalidJson = """
        {"title":"","body":"x"}
        """;

        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "en")
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Validation")))
                .andExpect(jsonPath("$.message", containsString("Title is required"))); // <- changed
    }



}
