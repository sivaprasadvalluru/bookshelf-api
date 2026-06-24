# BookShelf API

Personal book catalog REST API for PageTurner Labs book clubs.

## Prerequisites

- Java 17+
- Maven 3.9+

## Build

```bash
mvn clean package
```

The runnable JAR is produced at `target/bookshelf-api-0.0.1-SNAPSHOT.jar`.

## Run

```bash
# Using the Maven plugin (development)
mvn spring-boot:run

# Using the packaged JAR
java -jar target/bookshelf-api-0.0.1-SNAPSHOT.jar
```

The application starts on **port 8080**.

## Verify Health

```bash
curl http://localhost:8080/actuator/health
```

Expected response:

```json
{"status":"UP","components":{"db":{"status":"UP",...},...}}
```

## Run Tests

```bash
mvn test
```

## H2 Database

The application uses H2 in **file mode**. The database is stored at `./data/bookshelf.mv.db`
relative to the working directory and is created automatically on first start.

> **Note:** Only one JVM instance can open the H2 file database at a time.
> Stop any running instance before starting a second one, or you will get a
> file lock error.

The H2 console is available at <http://localhost:8080/h2-console> when the
application is running (local dev only).

