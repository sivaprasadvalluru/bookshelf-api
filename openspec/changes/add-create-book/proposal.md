## Why

The catalog is currently read-only. Book club administrators need to add new books to the catalog without direct DB access — this change adds the first write endpoint so the API manages its own data.

## What Changes

- `POST /api/books` accepts `{"title", "author", "isbn"}` and returns `201 Created` with the persisted book (including generated `id`)
- Duplicate ISBN detection: if a book with the same `isbn` already exists, the endpoint returns `409 Conflict`
- Input validation: `title`, `author`, and `isbn` are required; missing/blank values return `400 Bad Request`
- MockMvc tests covering success, duplicate ISBN, and missing field cases

## Capabilities

### New Capabilities

- `create-book`: `POST /api/books` — creates a new book; returns `201` on success, `409` on duplicate ISBN, `400` on missing required fields.

### Modified Capabilities

_(none — `list-books` and `health` spec requirements are unchanged)_

## Impact

- Modified: `BookController.java` (add `POST` handler), `Book.java` (add `isbn` unique constraint)
- New: `BookRequest.java` (request body record with Bean Validation), `GlobalExceptionHandler.java` (maps constraint violations and conflict to HTTP status)
- Adds dependency: `spring-boot-starter-validation`
- No breaking changes to `GET /api/books` or `/actuator/health`
