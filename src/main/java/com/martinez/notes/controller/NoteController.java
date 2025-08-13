package com.martinez.notes.controller;

import com.martinez.notes.dto.CreateNoteRequest;
import com.martinez.notes.dto.UpdateNoteRequest;
import com.martinez.notes.model.Note;
import com.martinez.notes.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    // POST /notes
    @PostMapping
    public ResponseEntity<Note> create(@Valid @RequestBody CreateNoteRequest req) {
        Note created = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /notes
    @GetMapping
    public List<Note> all() {
        return service.all();
    }

    // GET /notes/{id}
    @GetMapping("/{id}")
    public Note one(@PathVariable("id") Long id) {
        return service.one(id);
    }


    // PUT /notes/{id}
    @PutMapping("/{id}")
    public Note update(@PathVariable("id") Long id, @Valid @RequestBody UpdateNoteRequest req) {
        return service.update(id, req);
    }

    // DELETE /notes/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
