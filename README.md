# Sport Events Calendar

A web application for browsing and managing sport events.

## Overview

The app lets you:

- Browse a list of sport events with filtering by sport and date
- View detailed match information (teams, score, venue, stage, competition)
- Add new sport events via a web form

Seed data includes AFC Champions League 2024 fixtures to demonstrate the application out of the box.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.4 |
| Template Engine | Thymeleaf |
| CSS | Bootstrap 5.3.3 (CDN) |
| Database | PostgreSQL |
| Migrations | Flyway |
| ORM | Spring Data JPA / Hibernate |
| API Docs | springdoc-openapi (Swagger UI) |
| Build Tool | Maven (wrapper included) |
| Testing | JUnit 5, TestContainers |

## Prerequisites

- Docker

## Running the Application

```bash
git clone <repo-url>
cd sport-events-calendar
docker-compose up --build
```

This starts two containers:

| Service | Port |
|---|---|
| PostgreSQL 16 | `localhost:5433` |
| Application | `http://localhost:8080` |

The application waits for the database to pass its health check before starting. Database schema and seed data are applied automatically via Flyway on first boot.

To stop and remove containers:

```bash
docker-compose down
```

## Running Tests

```bash
# Unit tests only (Mockito, no database required)
./mvnw test
```

All current tests are unit tests that use Mockito to mock dependencies — no database or Docker is needed to run them.

TestContainers and the Failsafe plugin are already configured in `pom.xml` for future integration tests. Any test class named `*IT.java` will be picked up by `./mvnw verify` and can use a real PostgreSQL container via TestContainers.

## Application URLs

| URL | Description |
|---|---|
| `http://localhost:8080/` | Redirects to event list |
| `http://localhost:8080/events` | Browse all events (filter by sport/date) |
| `http://localhost:8080/events/{id}` | Event detail page |
| `http://localhost:8080/events/new` | Add a new event |
| `http://localhost:8080/api/v1/events` | REST API — list events |
| `http://localhost:8080/api/v1/sports` | REST API — list sports |
| `http://localhost:8080/api/v1/teams` | REST API — list teams |
| `http://localhost:8080/api/v1/competitions` | REST API — list competitions |
| `http://localhost:8080/swagger-ui` | Swagger UI |
| `http://localhost:8080/api-docs` | OpenAPI JSON spec |

## Project Structure

```
src/main/java/com/artembredak/sporteventscalendar/
├── SportEventsCalendarApplication.java
├── config/                  # OpenAPI & Web MVC config
├── domain/
│   ├── model/               # Pure domain records (Event, Team, Competition, ...)
│   ├── repository/          # Repository interfaces (port)
│   └── usecase/             # Use case interfaces (port)
├── service/                 # Use case implementations
└── infrastructure/
    ├── web/
    │   ├── controller/      # MVC + REST controllers
    │   ├── dto/             # Request/Response DTOs
    │   └── exception/       # Global exception handler
    └── persistence/
        ├── entity/          # JPA entities
        ├── repository/      # JPA repositories + adapters
        └── mapper/          # Entity ↔ domain mappers

src/main/resources/
├── application.properties
├── db/migration/            # Flyway SQL migrations
└── templates/
    ├── layout/base.html     # Shared layout (navbar, footer)
    └── events/              # list.html, detail.html, form.html

src/test/java/com/artembredak/sporteventscalendar/
├── SportEventsCalendarApplicationTests.java
├── controller/              # Unit tests for all controllers (Mockito)
└── service/                 # Unit tests for service layer (Mockito)
```

## API Reference

### Events

```
GET  /api/v1/events           # List all events; optional ?sportId=&date=YYYY-MM-DD
GET  /api/v1/events/{id}      # Get event detail
POST /api/v1/events           # Create event
```

### Sports / Teams / Competitions

```
GET /api/v1/sports
GET /api/v1/teams
GET /api/v1/competitions
```

Full interactive documentation is available at `/swagger-ui`.

## Database Schema

Eight normalised tables:

```
sport ──< competition ──< event >── event_team >── team
                              └──< event_result
                              └── venue
                              └── stage
```

- **sport** — sport category (e.g., Football)
- **competition** — league/cup with season year
- **stage** — match stage (ROUND_OF_16, FINAL, etc.)
- **venue** — stadium / city
- **team** — club with slug, abbreviation, country code
- **event** — a single fixture with date, time, status (SCHEDULED / PLAYED / CANCELLED / POSTPONED)
- **event_team** — junction table assigning HOME and AWAY teams to an event
- **event_result** — 1:1 with event; stores home/away goals and winner

## Architecture Decisions

### Hexagonal (Ports & Adapters) Architecture

The domain layer (`domain/model`, `domain/repository`, `domain/usecase`) has zero dependencies on Spring or JPA. Persistence and web layers are adapters that implement the domain interfaces. This keeps business logic independently testable and makes it easy to swap infrastructure (e.g., replace PostgreSQL with another database) without touching the domain.

### Dual Interface: MVC + REST API

The application exposes both a server-rendered Thymeleaf UI and a JSON REST API from the same service layer. The MVC controller (`EventCalendarController`) and the REST controller (`EventController`) share the same use cases. This allows the UI to be replaced or supplemented by a separate frontend later without any backend changes.

### Flyway for Schema Management

Flyway was chosen over `spring.jpa.hibernate.ddl-auto=create` to ensure reproducible, version-controlled schema evolution. `ddl-auto` is set to `validate` so Hibernate checks the schema on startup but never modifies it.

### Domain Models as Java Records

All domain objects are immutable Java records. This enforces that the domain layer carries no mutable state and makes equality and serialisation straightforward.

### Seed Data in Migration

A `V2__seed_data.sql` migration seeds the database with real AFC Champions League 2024 fixtures so the application is immediately usable and demonstrable without manual data entry.

### Mockito Unit Tests

Unit tests cover both the controller and service layers using Mockito. All use cases / repositories are mocked so tests run without a database and finish in milliseconds. Test classes live in a flattened `controller/` and `service/` package directly under the test root, mirroring the source structure without the `infrastructure.web` prefix.