# Implementation Plan: Hero GET API test coverage

**Branch**: `001-hero-get-api-tests` | **Date**: 2026-09-15 | **Spec**: `specs/001-hero-get-api-tests/spec.md`

**Input**: Feature specification from `/specs/001-hero-get-api-tests/spec.md`

## Summary

This feature plans automated tests only for the existing Hero GET API, without changing production code. The test suite will cover the successful retrieval path for Hero ID 1 and the missing-hero failure path as it behaves today in the application.

The implementation uses:
- JUnit 5 for test execution and assertions
- Mockito for `HeroService` unit tests
- MockMvc for `HeroController` HTTP-level API tests

The tests will validate the response contract for `HeroResponseDTO` fields:
- `id`
- `title`
- `description`
- `customerText`

The missing-Hero scenario is explicitly expected to throw `RuntimeException("Hero not found")` from `HeroService.getHero()`, which currently results in HTTP 500 in the controller layer because there is no custom exception handler in the current codebase.

## Technical Context

**Language/Version**: Java 25

**Primary Dependencies**: Spring Boot 4.1.1, Spring WebMVC, Spring Validation, Spring Security, Spring Data JPA

**Storage**: PostgreSQL via Spring Data JPA configuration (repository integration is present, but this feature only tests the API behavior and not persistence logic)

**Testing**: JUnit 5, Mockito, MockMvc, Spring Boot test starter

**Target Platform**: Backend REST API service

**Project Type**: Web service

**Performance Goals**: Not a performance focus for this feature; validation is behavior- and contract-focused

**Constraints**:
- No production code modification
- No POST, PUT, PATCH, or DELETE tests
- Only the GET `/api/v1/hero` behavior is in scope
- Current application behavior for missing hero is a runtime exception mapped to HTTP 500

**Scale/Scope**: Small API test effort focused on a single controller and service contract

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

The project constitution file at `.specify/memory/constitution.md` is still a template and does not define project-specific governance, so there are no active technological or process gates to enforce for this test-only feature. This plan remains aligned with the stated requirement to avoid production code changes and to focus only on the Hero GET flow.

## Project Structure

### Documentation (this feature)

```text
specs/001-hero-get-api-tests/
├── spec.md
├── plan.md
└── tasks.md   # to be created later via /speckit-tasks, not part of this planning step
```

### Source Code (repository root)

```text
src/
├── main/java/com/durrmi/backend/
│   ├── hero/
│   │   ├── controller/HeroController.java
│   │   ├── dto/HeroRequestDTO.java
│   │   ├── dto/HeroResponseDTO.java
│   │   ├── entity/Hero.java
│   │   ├── repository/HeroRepository.java
│   │   └── service/HeroService.java
│   └── ...
└── test/java/com/durrmi/backend/
    └── hero/
        ├── controller/HeroControllerTest.java
        └── service/HeroServiceTest.java
```

**Structure Decision**: Keep the feature test work within the existing Spring Boot package layout, adding test classes under the corresponding `hero` package under `src/test/java`. No new production packages or APIs are required.

## Test Strategy

### Phase 1: Service-layer unit tests

Purpose: validate the business behavior of `HeroService.getHero()` in isolation using Mockito.

Planned coverage:
1. Hero exists for ID 1
   - Mock `HeroRepository.findById(1L)` to return a `Hero` entity
   - Call `heroService.getHero()`
   - Assert returned `HeroResponseDTO` is not null
   - Assert values for `id`, `title`, `description`, and `customerText`

2. Hero missing for ID 1
   - Mock `HeroRepository.findById(1L)` to return `Optional.empty()`
   - Assert `RuntimeException` is thrown with message `"Hero not found"`

Test class target:
- `src/test/java/com/durrmi/backend/hero/service/HeroServiceTest.java`

### Phase 2: Controller/API tests with MockMvc

Purpose: validate the HTTP contract exposed by `HeroController.getHero()` without changing production code.

Planned coverage:
1. Hero exists
   - Mock `HeroService.getHero()` to return a `HeroResponseDTO` with known values
   - Send `GET /api/v1/hero`
   - Assert HTTP status is `200 OK`
   - Assert JSON contains `id`, `title`, `description`, and `customerText`
   - Assert values match the expected payload

2. Hero missing
   - Mock `HeroService.getHero()` to throw `RuntimeException("Hero not found")`
   - Send `GET /api/v1/hero`
   - Assert HTTP status is `500 Internal Server Error`
   - Assert no successful `HeroResponseDTO` payload is returned

Test class target:
- `src/test/java/com/durrmi/backend/hero/controller/HeroControllerTest.java`

## Scope and Exclusions

In-scope:
- `GET /api/v1/hero`
- Hero ID 1 exists scenario
- Hero ID 1 missing scenario
- `HeroResponseDTO` field validation
- JUnit 5 + Mockito + MockMvc testing

Out of scope:
- POST, PUT, PATCH, or DELETE endpoints
- production code changes
- database integration tests beyond the mocked repository behavior
- any non-Hero API or helper logic

## Validation Approach

The implementation will be considered complete when the test suite confirms:
- service success path returns the correct DTO
- service missing path throws `RuntimeException("Hero not found")`
- controller success path responds with `200 OK` and correct JSON payload
- controller missing path responds with `500 Internal Server Error` consistent with current application behavior

## Complexity Tracking

No constitution-based exceptions or simplification tradeoffs are required for this feature because the plan remains within the existing Spring Boot architecture and the established behavior of the current application.
