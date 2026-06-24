## Context

`GET /api/books` is live and returning seed data. This change adds the first mutating endpoint. It introduces three new concerns that don't exist yet: input validation, uniqueness enforcement, and error response shaping. Each needs a deliberate design decision because they establish patterns all future write endpoints will follow.

## Goals / Non-Goals

**Goals:**
- `POST /api/books` accepts `{"title", "author", "isbn"}`, persists the book, returns `201 Created` with the full book JSON (including `id`)
- `409 Conflict` when an existing book shares the same `isbn`
- `400 Bad Request` when any of `title`, `author`, or `isbn` is missing or blank
- A `@ControllerAdvice` exception handler that maps exceptions to HTTP responses — the central error-handling point for all future endpoints

**Non-Goals:**
- No partial updates (`PATCH`)
- No ISBN format validation (format, checksum) — just non-blank
- No authentication or authorization
- No pagination of error details

## Decisions

### 1 — Bean Validation (`@Valid`) for input

**Decision**: Add `spring-boot-starter-validation` and annotate the request body record with `@NotBlank` on each field; use `@Valid` in the controller.

**Rationale**: Declarative, zero-boilerplate, integrates with `MethodArgumentNotValidException` which Spring automatically converts. Consistent with Spring Boot idioms — future endpoints adopt the same pattern for free.

**Alternative considered**: Manual null checks in the controller — verbose and doesn't scale as the model grows.

### 2 — `@UniqueConstraint` on `isbn` + service-layer duplicate check

**Decision**: Add `@Column(unique = true)` on `Book.isbn` for DB-level enforcement, and add an explicit `BookRepository.existsByIsbn(String)` check in the controller before saving, throwing a custom `DuplicateIsbnException`.

**Rationale**: The DB constraint is the ultimate safety net (prevents duplicates even under concurrent inserts), but the application-layer check gives us a clean, catchable signal to map to `409` without parsing `DataIntegrityViolationException` messages. The controller stays simple: check → save → return.

**Alternative considered**: Catch `DataIntegrityViolationException` from JPA — couples error handling to DB exception details; unreliable across databases.

### 3 — `@ControllerAdvice` (`GlobalExceptionHandler`) for HTTP error mapping

**Decision**: Single `@ControllerAdvice` class in `com.pageturner.bookshelf` that maps:
- `DuplicateIsbnException` → `409 Conflict`
- `MethodArgumentNotValidException` → `400 Bad Request` (Spring fires this for `@Valid` failures)

**Rationale**: Keeps controller methods free of `try/catch`. Centralises all error-to-HTTP mapping in one place. Follows standard Spring Boot best practice.

**Alternative considered**: `ResponseStatusException` thrown directly in the controller — works but scatters error logic across controllers as they multiply.

### 4 — Request body as a `record` (`BookRequest`)

**Decision**: Introduce `BookRequest(String title, String author, String isbn)` as the POST body. The existing `Book` entity is not used directly as the request body.

**Rationale**: Separates the API contract from the persistence model. Even though the fields are the same today, accepting a `Book` entity as a request body would expose the `id` field (which clients must not supply) and tightly couples the API shape to the JPA model.

**Alternative considered**: Reuse `Book` as request body — violates API/entity separation; exposes `id`.

## Risks / Trade-offs

- **Race condition on duplicate check** → Check-then-save is not atomic; two concurrent POSTs with the same ISBN could both pass the check and then one fails at the DB constraint level with `DataIntegrityViolationException`. Mitigation: catch `DataIntegrityViolationException` as a fallback and return `409` too. Not needed at this scale but worth noting.
- **Validation error response shape** → `MethodArgumentNotValidException` produces a verbose default body. Mitigation: the `GlobalExceptionHandler` will return a minimal `{"error": "..."}` shape.

## Migration Plan

No schema migration needed — adding `UNIQUE` constraint on `isbn` via `ddl-auto=update` is applied automatically. If the existing seed data had duplicate ISBNs it would fail; the three seed books have distinct ISBNs so this is safe.
