## ADDED Requirements

### Requirement: List all books
The system SHALL expose `GET /api/books` returning HTTP 200 with a JSON array of all books in the catalog. Each book object SHALL include `id` (integer), `title` (string), `author` (string), and `isbn` (string).

#### Scenario: Returns all books as a JSON array
- **WHEN** a client sends `GET /api/books`
- **THEN** the response status is 200
- **AND** the response body is a JSON array
- **AND** each element contains the fields `id`, `title`, `author`, and `isbn`

#### Scenario: Returns seed books on a fresh start
- **WHEN** the application starts with the default seed data
- **AND** a client sends `GET /api/books`
- **THEN** the response array contains books with titles "Dune", "Foundation", and "Neuromancer"

#### Scenario: Returns empty array when no books exist
- **WHEN** the catalog contains no books
- **AND** a client sends `GET /api/books`
- **THEN** the response status is 200
- **AND** the response body is `[]`

### Requirement: Book data model
The system SHALL persist books in a `book` table with columns `id` (auto-generated primary key), `title` (not null), `author` (not null), and `isbn` (not null).

#### Scenario: Book entity is persisted with all required fields
- **WHEN** a `Book` record with title, author, and isbn is saved via the repository
- **THEN** the record is retrievable by id with all fields intact

#### Scenario: Seed data is present at startup
- **WHEN** the application starts
- **THEN** the `book` table contains at least the three seed books: Dune (Frank Herbert, ISBN 978-0-441-17271-9), Foundation (Isaac Asimov, ISBN 978-0-553-29335-7), and Neuromancer (William Gibson, ISBN 978-0-441-56956-4)
