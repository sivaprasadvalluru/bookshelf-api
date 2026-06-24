## Context

The project scaffold from `bootstrap-bookshelf-api` is in place: Spring Boot 3.4, H2 file DB, Actuator health. This change introduces the first domain layer — a `Book` entity, its JPA repository, and a read-only list endpoint. No write operations are in scope.

## Goals / Non-Goals

**Goals:**
- `Book` entity mapped to the `book` table with `id`, `title`, `author`, `isbn`
- `BookRepository` for JPA access (no custom queries needed)
- `GET /api/books` returns `200 OK` with a JSON array of all books
- `data.sql` seeded with three books: Dune, Foundation, Neuromancer
- MockMvc test covering the list endpoint response shape and seed data presence

**Non-Goals:**
- No `POST`, `PUT`, `DELETE` — read-only for now
- No pagination or filtering
- No `GET /api/books/{id}` single-book lookup
- No DTO mapping layer — entity fields can be serialized directly for this change
- No ISBN format validation

## Decisions

### 1 — Entity serialized directly (no DTO)

**Decision**: Serialize the `Book` JPA entity directly via Jackson; no separate DTO class.

**Rationale**: With four flat scalar fields and no sensitive or computed data, a DTO adds indirection with zero benefit at this stage. A future change can introduce a DTO when the entity diverges from the desired API shape.

**Alternative considered**: `BookDto` record — overhead with no current benefit.

### 2 — `@RestController` over Spring Data REST

**Decision**: Explicit `BookController` with `@GetMapping("/api/books")`.

**Rationale**: Spring Data REST auto-exposes HATEOAS-wrapped responses (`_embedded`, pagination links) which don't match the flat JSON array the spec requires. An explicit controller is a handful of lines and gives full control over response shape.

**Alternative considered**: `spring-boot-starter-data-rest` — discarded; response shape mismatch.

### 3 — `data.sql` for seed data

**Decision**: Insert seed rows in `src/main/resources/data.sql`.

**Rationale**: Already established as the project convention; Spring Boot executes it at startup. `ddl-auto=update` keeps the schema between runs; `data.sql` is idempotent only if rows don't exist — avoid duplicates by using `MERGE INTO` or `INSERT INTO ... WHERE NOT EXISTS` for H2.

## Risks / Trade-offs

- **Duplicate seed rows on restart** → H2 file mode persists data; plain `INSERT` in `data.sql` would duplicate rows. Mitigation: use `MERGE INTO` (H2 syntax) keyed on `id`.
- **Entity-as-DTO coupling** → Jackson serializes all non-transient fields, including any future internal fields added to `Book`. Mitigation: acceptable now; document the DTO refactor as a follow-up.
- **Health endpoint regression** → Adding JPA entities doesn't affect Actuator; Hibernate DDL `update` is safe against a schema it already owns. No mitigation needed beyond running the existing health test.
