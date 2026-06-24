-- Seed data for local development
-- MERGE INTO is idempotent: safe to re-run against the H2 file database
MERGE INTO book (id, title, author, isbn) KEY(id)
    VALUES (1, 'Dune', 'Frank Herbert', '978-0-441-17271-9');
MERGE INTO book (id, title, author, isbn) KEY(id)
    VALUES (2, 'Foundation', 'Isaac Asimov', '978-0-553-29335-7');
MERGE INTO book (id, title, author, isbn) KEY(id)
    VALUES (3, 'Neuromancer', 'William Gibson', '978-0-441-56956-4');
-- Reset the identity sequence so app-generated IDs don't collide with seed IDs
ALTER TABLE book ALTER COLUMN id RESTART WITH 100;
