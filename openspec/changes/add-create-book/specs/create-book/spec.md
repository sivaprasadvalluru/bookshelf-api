## ADDED Requirements

### Requirement: Create a book
The system SHALL expose `POST /api/books` accepting a JSON request body with `title`, `author`, and `isbn`. On success the system SHALL persist the book, return HTTP `201 Created`, and include the full book object (with generated `id`) in the response body.

#### Scenario: Successful book creation
- **WHEN** a client sends `POST /api/books` with body `{"title":"1984","author":"George Orwell","isbn":"978-0-452-28423-4"}`
- **THEN** the response status is 201
- **AND** the response body contains `"title":"1984"`, `"author":"George Orwell"`, `"isbn":"978-0-452-28423-4"`
- **AND** the response body contains a non-null `id`

#### Scenario: Created book is retrievable via GET
- **WHEN** a client creates a book via `POST /api/books`
- **AND** then sends `GET /api/books`
- **THEN** the newly created book appears in the response array

### Requirement: Reject duplicate ISBN
The system SHALL return HTTP `409 Conflict` when a `POST /api/books` request contains an `isbn` that already exists in the catalog.

#### Scenario: Duplicate ISBN returns 409
- **WHEN** a book with `isbn` "978-0-441-17271-9" already exists in the catalog
- **AND** a client sends `POST /api/books` with `isbn` "978-0-441-17271-9"
- **THEN** the response status is 409
- **AND** no duplicate book is persisted

#### Scenario: Different ISBN is accepted
- **WHEN** a client sends `POST /api/books` with an `isbn` not already in the catalog
- **THEN** the response status is 201

### Requirement: Validate required fields
The system SHALL return HTTP `400 Bad Request` when any of `title`, `author`, or `isbn` is missing or blank in the request body.

#### Scenario: Missing title returns 400
- **WHEN** a client sends `POST /api/books` with body `{"author":"Frank Herbert","isbn":"978-0-000-00000-1"}`
- **THEN** the response status is 400

#### Scenario: Blank author returns 400
- **WHEN** a client sends `POST /api/books` with body `{"title":"Dune","author":"","isbn":"978-0-000-00000-1"}`
- **THEN** the response status is 400

#### Scenario: Missing isbn returns 400
- **WHEN** a client sends `POST /api/books` with body `{"title":"Dune","author":"Frank Herbert"}`
- **THEN** the response status is 400
