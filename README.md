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
  - **Logged-in users** — can browse leagues, teams, players and matches, and submit create/edit/delete
    proposals for them (see [Change Approval Workflow](#change-approval-workflow) below).
  - **Admins** — create/edit/delete actions apply immediately, and admins additionally review,
    approve or reject proposals submitted by regular users
    (`@PreAuthorize("hasRole('ADMIN')")` on the admin review endpoints).
- The first user to register is automatically assigned the `ADMIN` role.

## Functionalities

- Create / edit / delete a **League**
- Create / edit / delete a **Team** (with league selection)
- Create / edit / delete a **Player** (with team selection)
- Create / edit / delete a **Match** (home/away team selection and date/time)
- Record / edit / delete a **Goal** within a match (scorer, optional assist, minute 1–40); the half
  is derived automatically from the minute (1–20 → 1st half, 21–40 → 2nd half) and the match score
  is recalculated automatically after every goal change

Each functionality is triggered through a form/button in the UI, invokes a backend endpoint, and
shows a visible result (redirect to the updated list).

## Change Approval Workflow

Regular (non-admin) users don't write directly to the database. When a logged-in `USER` submits a
create, edit or delete action on a League, Team, Player or Match, the request is stored as a
**pending `ChangeRequest`** instead of being applied immediately. Admins act on these from a
dedicated **"Pending changes"** page:

- **Approve** — re-runs the same validation and business rules as a direct admin action (so a
  proposal that has gone stale, e.g. a shirt number taken by someone else in the meantime, fails
  gracefully with an inline error and stays pending rather than crashing or silently corrupting
  data), then applies the change for real.
- **Reject** — discards the proposal with an optional reason (the admin can pick from
  domain-specific suggested reasons or type a custom one).

Users can track their own submissions, including rejection reasons, on a **"My proposals"** page,
and resubmit a corrected version of a rejected create/edit proposal with one click (the form is
pre-filled with their original input).

Admin actions are unaffected by this workflow — they still apply immediately, exactly as before.

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
