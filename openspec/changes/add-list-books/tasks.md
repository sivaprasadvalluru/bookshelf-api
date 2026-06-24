## 1. Domain Model

- [x] 1.1 Create `src/main/java/com/pageturner/bookshelf/book/Book.java` — JPA entity with `@Id @GeneratedValue` `id`, `title`, `author`, `isbn` (all non-null)
- [x] 1.2 Create `src/main/java/com/pageturner/bookshelf/book/BookRepository.java` — interface extending `JpaRepository<Book, Long>`

## 2. REST Endpoint

- [x] 2.1 Create `src/main/java/com/pageturner/bookshelf/book/BookController.java` — `@RestController` with `GET /api/books` returning `List<Book>` via `BookRepository.findAll()`

## 3. Seed Data

- [x] 3.1 Replace the placeholder comment in `src/main/resources/data.sql` with `MERGE INTO` statements for the three seed books: Dune (Frank Herbert, ISBN 978-0-441-17271-9), Foundation (Isaac Asimov, ISBN 978-0-553-29335-7), Neuromancer (William Gibson, ISBN 978-0-441-56956-4)

## 4. Tests

- [x] 4.1 Create `src/test/java/com/pageturner/bookshelf/book/BookControllerTest.java` — `@WebMvcTest(BookController.class)` with `@MockitoBean BookRepository`; assert `GET /api/books` returns 200 and a JSON array
- [x] 4.2 Create `src/test/java/com/pageturner/bookshelf/book/BookControllerIT.java` — `@SpringBootTest(webEnvironment = RANDOM_PORT)` + `TestRestTemplate`; assert seed titles Dune, Foundation, Neuromancer are present in the response array
- [x] 4.3 Run `mvn test` and confirm all tests pass (existing health tests must also remain green)
