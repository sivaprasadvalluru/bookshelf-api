## Why

BookShelf API is a greenfield project with no existing code. This change bootstraps the Spring Boot project scaffold so subsequent changes can add real functionality on a working, tested foundation.

## What Changes

- New Maven project at root with Spring Boot 3.4, Java 17, package `com.pageturner.bookshelf`
- H2 file-mode database configured at `./data/bookshelf.mv.db`
- Spring Boot Actuator enabled; `GET /actuator/health` returns `{"status":"UP"}`
- `README.md` with build and run instructions

## Capabilities

### New Capabilities

- `health`: Actuator health endpoint — confirms the application starts and the Spring context loads correctly.

### Modified Capabilities

_(none — greenfield)_

## Impact

- Creates the entire project structure (`pom.xml`, `src/`, `application.properties`, `README.md`)
- No existing code is modified; no breaking changes
- Introduces dependencies: `spring-boot-starter-web`, `spring-boot-starter-actuator`, `spring-boot-starter-data-jpa`, `h2`, `spring-boot-starter-test`
