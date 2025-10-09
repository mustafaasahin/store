# Store Service

Java 24 Spring Boot application that manages accounts through a REST API. Persistence is handled by MySQL with Flyway providing schema migrations. A multi-stage Docker build and docker-compose stack make it easy to run locally or deploy as containers.


## Prerequisites
- Docker 20.10+
- Docker Compose v2+

The Maven wrapper and JDK are provided in the Docker images, so no local Java toolchain is required for container workflows.

## Running with Docker
1. Build and start the stack:
   ```bash
   docker compose up --build
   ```
   The first run downloads Maven dependencies and the MySQL base image.

2. Once both services are healthy:
   - REST API: http://localhost:8080
   - MySQL: host `localhost`, port `3306`, database `store`, user `store`, password `storepass`. The MySQL root password is `rootpass`.

3. Stop the stack when finished:
   ```bash
   docker compose down
   ```
   Add `-v` to drop the persistent MySQL volume if you want a fresh schema on next start.

Flyway runs automatically on startup and applies migrations located under `src/main/resources/db/migration`.

## Configuration
The application reads database details from environment variables with sensible defaults. Override them in `docker-compose.yml` or at runtime when needed:

| Variable        | Default   | Purpose                  |
|-----------------|-----------|--------------------------|
| `DB_HOST`       | `mysql`   | MySQL service hostname   |
| `DB_PORT`       | `3306`    | MySQL port               |
| `DB_NAME`       | `store`   | Database schema name     |
| `DB_USERNAME`   | `store`   | Application DB user      |
| `DB_PASSWORD`   | `storepass` | Application DB password |
| `SPRING_PROFILES_ACTIVE` | `prod` | Spring Boot profile |

## Useful Commands
- Rebuild only the application image: `docker compose build app`
- Tail application logs: `docker compose logs -f app`
- Run Flyway or database migrations from scratch: `docker compose down -v && docker compose up --build`

## Local Development (Optional)
If you prefer running Maven directly:
```bash
./mvnw spring-boot:run
```
Point `application.properties` to your local MySQL instance or export the same environment variables used in Docker so that the application can connect.

## API Overview
- `POST /account` — Create a new account (JSON body with `name` and `email`).
- `GET /account?name=...&email=...` — Fetch an account by email (and name for logging).

Refer to the controllers under `src/main/java/com/spring/store/controller` for details and extend as needed.
