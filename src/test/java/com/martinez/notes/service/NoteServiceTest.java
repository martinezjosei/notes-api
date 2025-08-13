package com.martinez.notes.service;

import com.martinez.notes.dto.CreateNoteRequest;
import com.martinez.notes.dto.UpdateNoteRequest;
import com.martinez.notes.exception.NoteNotFoundException;
import com.martinez.notes.model.Note;
import com.martinez.notes.repository.InMemoryNoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock InMemoryNoteRepository repo;
    @InjectMocks NoteService service;

    private Note note(long id, String title, String body) {
        var now = LocalDateTime.now();
        return new Note(id, title, body, now.minusMinutes(2), now.minusMinutes(1));
    }

    @Test
    @DisplayName("create -> delegates to repo.save with title/body")
    void createDelegatesToSave() {
        var req = new CreateNoteRequest("T", "B");
        var saved = note(1, "T", "B");

        when(repo.save("T", "B")).thenReturn(saved);

        var result = service.create(req);

        assertEquals(1L, result.id());
        verify(repo).save("T", "B");
        verifyNoMoreInteractions(repo);
    }

    @Test
    @DisplayName("all -> returns repository results")
    void allReturnsList() {
        when(repo.findAll()).thenReturn(List.of(note(1, "A", "B")));

        var list = service.all();

        assertEquals(1, list.size());
        verify(repo).findAll();
        verifyNoMoreInteractions(repo);
    }

    @Test
    @DisplayName("one -> returns note when found")
    void oneFound() {
        when(repo.findById(5L)).thenReturn(Optional.of(note(5, "X", "Y")));

        var n = service.one(5L);

        assertEquals(5L, n.id());
        verify(repo).findById(5L);
        verifyNoMoreInteractions(repo);
    }

    @Test
    @DisplayName("one -> throws NoteNotFoundException when missing")
    void oneMissing() {
        when(repo.findById(42L)).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> service.one(42L));

        verify(repo).findById(42L);
        verifyNoMoreInteractions(repo);
    }

    @Test
    @DisplayName("update -> returns updated note when repo updates")
    void updateOk() {
        var req = new UpdateNoteRequest("New", "Body");
        var updated = note(3, "New", "Body");
        when(repo.update(3L, "New", "Body")).thenReturn(updated);

        var result = service.update(3L, req);

        assertEquals("New", result.title());
        assertEquals("Body", result.body());
        verify(repo).update(3L, "New", "Body");
        verifyNoMoreInteractions(repo);
    }

    @Test
    @DisplayName("update -> throws NoteNotFoundException when repo returns null")
    void updateMissing() {
        var req = new UpdateNoteRequest("New", "Body");
        when(repo.update(7L, "New", "Body")).thenReturn(null);

        assertThrows(NoteNotFoundException.class, () -> service.update(7L, req));

        verify(repo).update(7L, "New", "Body");
        verifyNoMoreInteractions(repo);
    }

    @Test
    @DisplayName("delete -> ok when repo.delete returns true; throws when false")
    void deleteBehavior() {
        when(repo.delete(9L)).thenReturn(true);
        assertDoesNotThrow(() -> service.delete(9L));
        verify(repo).delete(9L);

        when(repo.delete(10L)).thenReturn(false);
        assertThrows(NoteNotFoundException.class, () -> service.delete(10L));
        verify(repo).delete(10L);

        verifyNoMoreInteractions(repo);
    }
}
