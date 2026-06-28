# Football League Manager

Spring Boot application for managing football leagues, teams, players and matches.
Built as the Individual Project Assignment for the Spring Fundamentals course @ SoftUni.

## Tech Stack

- Java 17+
- Spring Boot 4.1.0
- Spring MVC + Thymeleaf
- Spring Data JPA (Hibernate)
- Spring Security (session-based authentication)
- MySQL
- Maven
- Lombok

## Domain Model

- **League** — a football league
- **Team** — belongs to a League
- **Player** — belongs to a Team
- **Match** — played between two Teams (home/away) of a League

All entities use a `UUID` as their primary key. `League → Team`, `Team → Player` and `Team → Match`
(home/away) are modeled as entity relationships.

Technical entities (not counted as domain entities): **User**, **Role** — used for authentication only.

## Security

- Session-based login/registration with BCrypt-hashed passwords.
- Access control:
  - **Guests** — can only access the home page, register and login pages.
  - **Logged-in users** — can browse leagues, teams, players and matches.
  - **Admins** — can additionally create, edit and delete leagues, teams, players and matches
    (`@PreAuthorize("hasRole('ADMIN')")` on controller mutation endpoints).
- The first user to register is automatically assigned the `ADMIN` role.

## Functionalities

- Create / edit / delete a **League**
- Create / edit / delete a **Team** (with league selection)
- Create / edit / delete a **Player** (with team selection)
- Create / edit / delete a **Match** (with home/away team selection, score and date/time)

Each functionality is triggered through a form/button in the UI, invokes a backend endpoint, and
shows a visible result (redirect to the updated list).

## Validation & Error Handling

- Every form has server-side validation (`@Valid` + Bean Validation annotations on DTOs).
- Invalid submissions redisplay the form with field-level error messages shown in red.
- Business rule example: a match's home team and away team must be different — enforced both as a
  field-level validation error in the form and as a guard in the service layer
  (`InvalidMatchException`).
- Custom exceptions (`EntityNotFoundException`, `InvalidMatchException`,
  `UsernameAlreadyExistsException`) are handled centrally by a `@ControllerAdvice`
  (`GlobalExceptionHandler`), which renders a friendly error page instead of a default error page.

## Database (Docker)

The project ships a `docker-compose.yml` that starts a MySQL 8.0 container matching the
credentials in `application.properties` out of the box — no manual MySQL install needed:

```
docker-compose up -d
```

This creates the `football_league_manager` database on `localhost:3306` with root password
`12345` (override with `MYSQL_ROOT_PASSWORD=yourpassword docker-compose up -d` if you change it
in `application.properties` too). Data persists in a named Docker volume across restarts.

## Running locally

1. Start the database: `docker-compose up -d` (see above), or point
   `src/main/resources/application.properties` at your own MySQL instance.
2. Run:

   ```
   ./mvnw spring-boot:run
   ```

3. Open `http://localhost:8080`, register a user (the first one becomes `ADMIN`), and log in.
