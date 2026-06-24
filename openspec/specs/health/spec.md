# health Specification

## Purpose
TBD - created by archiving change bootstrap-bookshelf-api. Update Purpose after archive.
## Requirements
### Requirement: Application starts successfully
The system SHALL start as a runnable Spring Boot application using `mvn spring-boot:run` or by executing the packaged JAR. The Spring application context SHALL load without errors.

#### Scenario: Application starts with default config
- **WHEN** the application is launched with no overriding environment variables
- **THEN** the Spring context loads and the embedded Tomcat server binds to port 8080

#### Scenario: Application builds cleanly
- **WHEN** `mvn clean package` is executed
- **THEN** the build succeeds with exit code 0 and a runnable JAR is produced in `target/`

### Requirement: Health endpoint returns UP
The system SHALL expose `GET /actuator/health` which returns HTTP 200 with body `{"status":"UP"}` when the application and database are healthy.

#### Scenario: Health check on a running application
- **WHEN** a client sends `GET /actuator/health`
- **THEN** the response status is 200
- **AND** the response body contains `"status": "UP"`

#### Scenario: Health check includes database status
- **WHEN** a client sends `GET /actuator/health`
- **THEN** the response body contains a `db` component with status `UP`

### Requirement: H2 file database is configured
The system SHALL use H2 in file mode, storing data at `./data/bookshelf.mv.db`. The database SHALL be created automatically on first start if it does not exist.

#### Scenario: Database file is created on first run
- **WHEN** the application starts for the first time and `./data/bookshelf.mv.db` does not exist
- **THEN** H2 creates the file and the application starts without error

#### Scenario: Database persists across restarts
- **WHEN** the application is stopped and restarted
- **THEN** the `./data/bookshelf.mv.db` file remains intact and is reused

### Requirement: README provides build and run instructions
The project SHALL include a `README.md` at the repository root that documents how to build and run the application locally.

#### Scenario: Developer follows README to run the app
- **WHEN** a developer follows the README instructions (`mvn spring-boot:run`)
- **THEN** the application starts and `GET /actuator/health` returns `{"status":"UP"}`

#### Scenario: README documents test execution
- **WHEN** a developer runs `mvn test` as described in the README
- **THEN** all tests pass and the command exits with code 0

