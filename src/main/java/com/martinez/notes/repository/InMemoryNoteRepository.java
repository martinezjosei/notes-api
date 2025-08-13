package com.martinez.notes.repository;

import com.martinez.notes.model.Note;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryNoteRepository {

    private final Map<Long, Note> store = new ConcurrentHashMap<>();
    private final AtomicLong idSeq = new AtomicLong(1);


    public Note save(String title, String body) {
        Long id = idSeq.getAndIncrement();
        LocalDateTime now = LocalDateTime.now();
        Note n = new Note(id, title, body, now, now);
        store.put(id, n);
        return n;
    }

    public List<Note> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Note> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }


    public Note update(Long id, String title, String body) {
        Note existing = store.get(id);
        if (existing == null) return null;
        Note updated = new Note(
                id,
                title,
                body,
                existing.createdAt(),
                LocalDateTime.now()
        );
        store.put(id, updated);
        return updated;
    }


    public boolean delete(Long id) {
        return store.remove(id) != null;
    }
}
