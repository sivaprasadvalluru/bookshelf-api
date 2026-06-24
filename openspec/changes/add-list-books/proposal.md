## Why

The API scaffold exists but exposes no domain data. Book club members need to browse the catalog — this change adds the first domain endpoint so the API delivers actual value.

## What Changes

- New `Book` JPA entity with fields: `id`, `title`, `author`, `isbn`
- `BookRepository` extending `JpaRepository`
- `GET /api/books` endpoint returning a JSON array of all books
- `data.sql` seeded with three classic books: Dune, Foundation, Neuromancer
- MockMvc test covering the list endpoint and seed data

## Capabilities

### New Capabilities

- `list-books`: `GET /api/books` — returns all books as a JSON array; each item exposes `id`, `title`, `author`, and `isbn`.

### Modified Capabilities

_(none — `health` spec requirements are unchanged; the existing health endpoint continues to return UP)_

## Impact

- New files: `Book.java`, `BookRepository.java`, `BookController.java`, `BookControllerTest.java`
- Modified: `data.sql` (add seed rows)
- H2 schema auto-managed by Hibernate DDL (`update`) — no manual migration needed
- No breaking changes to `/actuator/health`
