package com.pageturner.bookshelf.book;

import com.pageturner.bookshelf.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(GlobalExceptionHandler.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookRepository bookRepository;

    // ── GET /api/books ────────────────────────────────────────────────────────

    @Test
    void listBooks_returnsOkAndJsonArray() throws Exception {
        when(bookRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void listBooks_returnsBooksFromRepository() throws Exception {
        Book book = new Book("Dune", "Frank Herbert", "978-0-441-17271-9");
        when(bookRepository.findAll()).thenReturn(List.of(book));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Dune"))
                .andExpect(jsonPath("$[0].author").value("Frank Herbert"))
                .andExpect(jsonPath("$[0].isbn").value("978-0-441-17271-9"));
    }

    // ── POST /api/books ───────────────────────────────────────────────────────

    @Test
    void createBook_returnsCreatedWithBody() throws Exception {
        Book saved = new Book("1984", "George Orwell", "978-0-452-28423-4");
        when(bookRepository.existsByIsbn("978-0-452-28423-4")).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(saved);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"1984","author":"George Orwell","isbn":"978-0-452-28423-4"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("1984"))
                .andExpect(jsonPath("$.author").value("George Orwell"))
                .andExpect(jsonPath("$.isbn").value("978-0-452-28423-4"));
    }

    @Test
    void createBook_duplicateIsbn_returns409() throws Exception {
        when(bookRepository.existsByIsbn("978-0-441-17271-9")).thenReturn(true);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Dune","author":"Frank Herbert","isbn":"978-0-441-17271-9"}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void createBook_missingTitle_returns400() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"author":"Frank Herbert","isbn":"978-0-000-00000-1"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBook_blankAuthor_returns400() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Dune","author":"","isbn":"978-0-000-00000-1"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBook_missingIsbn_returns400() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Dune","author":"Frank Herbert"}
                                """))
                .andExpect(status().isBadRequest());
    }
}
