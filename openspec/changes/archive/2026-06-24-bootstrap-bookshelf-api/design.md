## Context

BookShelf API is a net-new Spring Boot project. There is no existing code; this change lays the foundation that all future changes build on. The goal is a running application with a verified health endpoint and a reproducible developer experience.

## Goals / Non-Goals

**Goals:**
- Generate a valid Maven project with Spring Boot 3.4, Java 17, package `com.pageturner.bookshelf`
- Configure H2 in file mode at `./data/bookshelf.mv.db` (persists across restarts)
- Expose `GET /actuator/health` returning `{"status":"UP"}`
- Provide a `README.md` with `mvn spring-boot:run` and `mvn test` instructions

**Non-Goals:**
- No book, author, or any domain endpoints in this change
- No security / authentication layer
- No Docker or cloud deployment config
- No production database (PostgreSQL, etc.)

## Decisions

### 1 — H2 file mode over in-memory

**Decision**: Use `spring.datasource.url=jdbc:h2:file:./data/bookshelf` (file mode) rather than `jdbc:h2:mem`.

**Rationale**: File mode means state survives application restarts during local development, which better mirrors a real database and avoids confusion when future changes add seed data.

**Alternative considered**: `jdbc:h2:mem:bookshelf` — discarded because data is lost on every restart, making iterative development harder.

### 2 — Spring Boot Actuator for health

**Decision**: Use the out-of-the-box `spring-boot-starter-actuator`; expose only `/actuator/health`.

**Rationale**: Zero custom code required. The default `HealthIndicator` checks the DataSource automatically, so the health endpoint doubles as a DB connectivity check. Future changes can add custom indicators without touching this setup.

**Alternative considered**: Custom `@RestController` returning `"UP"` — adds code with no benefit over actuator.

### 3 — Maven over Gradle

**Decision**: Maven (`pom.xml`).

**Rationale**: The project context explicitly specifies Maven. No trade-off to evaluate.

## Risks / Trade-offs

- **H2 file locking** → Only one JVM instance can open the file database at a time. Mitigation: acceptable for local dev; document this in README.
- **Actuator exposure** → By default `/actuator/**` is open. Mitigation: non-issue at this stage; future security changes will restrict the exposure.

## Migration Plan

Greenfield — no migration needed. Steps to go live:

1. `mvn clean package`
2. `java -jar target/bookshelf-api-*.jar`
3. Confirm `GET /actuator/health` returns 200 `{"status":"UP"}`
