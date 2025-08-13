package com.martinez.notes.service;

import com.martinez.notes.dto.CreateNoteRequest;
import com.martinez.notes.dto.UpdateNoteRequest;
import com.martinez.notes.exception.NoteNotFoundException;
import com.martinez.notes.model.Note;
import com.martinez.notes.repository.InMemoryNoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final InMemoryNoteRepository repo;

    public NoteService(InMemoryNoteRepository repo) {
        this.repo = repo;
    }

    public Note create(CreateNoteRequest req) {
        return repo.save(req.title(), req.body());
    }

    public List<Note> all() {
        return repo.findAll();
    }

    public Note one(Long id) {
        return repo.findById(id).orElseThrow(() -> new NoteNotFoundException(id));
    }

    public Note update(Long id, UpdateNoteRequest req) {
        Note updated = repo.update(id, req.title(), req.body());
        if (updated == null) throw new NoteNotFoundException(id);
        return updated;
    }

    public void delete(Long id) {
        boolean removed = repo.delete(id);
        if (!removed) throw new NoteNotFoundException(id);
    }
}
