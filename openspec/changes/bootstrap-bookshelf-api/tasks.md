## 1. Maven Project Scaffold

- [ ] 1.1 Create `pom.xml` with Spring Boot 3.4 parent, Java 17, `com.pageturner.bookshelf` groupId/package, and dependencies: `spring-boot-starter-web`, `spring-boot-starter-actuator`, `spring-boot-starter-data-jpa`, `com.h2database:h2`, `spring-boot-starter-test`
- [ ] 1.2 Create `src/main/java/com/pageturner/bookshelf/BookshelfApplication.java` with `@SpringBootApplication` main class
- [ ] 1.3 Create `src/main/resources/application.properties` with H2 file datasource (`jdbc:h2:file:./data/bookshelf`), JPA DDL auto create, and H2 console enabled for local dev
- [ ] 1.4 Create `src/main/resources/data.sql` as an empty placeholder (no seed data yet)

## 2. Actuator Health Endpoint

- [ ] 2.1 Verify `management.endpoints.web.exposure.include=health` is set in `application.properties` (actuator exposes `/actuator/health` by default; confirm no custom exclusions)
- [ ] 2.2 Confirm `spring.datasource.*` properties include a named datasource so the default `DataSourceHealthIndicator` activates and reports `db: UP`

## 3. Tests

- [ ] 3.1 Create `src/test/java/com/pageturner/bookshelf/BookshelfApplicationTests.java` with a `@SpringBootTest` context-loads smoke test
- [ ] 3.2 Create `src/test/java/com/pageturner/bookshelf/ActuatorHealthIT.java` using `@SpringBootTest(webEnvironment = RANDOM_PORT)` + `TestRestTemplate` to assert `GET /actuator/health` returns 200 with `"status":"UP"`
- [ ] 3.3 Run `mvn test` and confirm all tests pass

## 4. README

- [ ] 4.1 Create `README.md` at the project root documenting: prerequisites (Java 17, Maven 3.9+), how to build (`mvn clean package`), how to run (`mvn spring-boot:run`), how to run tests (`mvn test`), and how to verify health (`curl http://localhost:8080/actuator/health`)
- [ ] 4.2 Add a note about H2 file location (`./data/bookshelf.mv.db`) and that only one JVM instance can open it at a time

## 5. Smoke Verification

- [ ] 5.1 Start the application with `mvn spring-boot:run` and confirm `GET http://localhost:8080/actuator/health` returns `{"status":"UP"}` manually or via curl
- [ ] 5.2 Confirm `./data/bookshelf.mv.db` is created on disk after first start
