## 1. Dependencies & Model

- [x] 1.1 Add `spring-boot-starter-validation` dependency to `pom.xml`
- [x] 1.2 Add `@Column(unique = true)` to the `isbn` field in `Book.java`
- [x] 1.3 Create `src/main/java/com/pageturner/bookshelf/book/BookRequest.java` — record with `@NotBlank String title`, `@NotBlank String author`, `@NotBlank String isbn`

## 2. Duplicate Detection

- [x] 2.1 Create `src/main/java/com/pageturner/bookshelf/book/DuplicateIsbnException.java` — unchecked exception carrying the conflicting ISBN
- [x] 2.2 Add `boolean existsByIsbn(String isbn)` to `BookRepository.java`

## 3. Controller & Error Handling

- [x] 3.1 Add `POST /api/books` handler to `BookController.java`: validate `@Valid @RequestBody BookRequest`, check `existsByIsbn`, save new `Book`, return `ResponseEntity` with `201 Created` and the saved book
- [x] 3.2 Create `src/main/java/com/pageturner/bookshelf/GlobalExceptionHandler.java` — `@RestControllerAdvice` mapping `DuplicateIsbnException` → `409` and `MethodArgumentNotValidException` → `400`, both returning `{"error": "<message>"}`

## 4. Tests

- [x] 4.1 Add POST tests to `BookControllerTest.java` (`@WebMvcTest`): success → 201 with body; duplicate ISBN → 409; missing title → 400; blank author → 400; missing isbn → 400
- [x] 4.2 Add `BookCreateIT.java` (`@SpringBootTest RANDOM_PORT`): POST a new book → 201, id present; POST same ISBN again → 409
- [x] 4.3 Run `mvn test` — all tests pass, no regressions on GET or health
