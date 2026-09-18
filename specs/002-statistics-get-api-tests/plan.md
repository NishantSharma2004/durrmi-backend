# Implementation Plan: Statistics GET API test coverage

**Branch**: `002-statistics-get-api-tests` | **Date**: 2026-09-15 | **Spec**: `specs/002-statistics-get-api-tests/spec.md`

**Input**: Feature specification from `/specs/002-statistics-get-api-tests/spec.md`

## Summary

This feature plans automated tests only for the existing Statistics GET API, without changing production code. The suite will validate the successful retrieval of the full statistics list and the empty-list behavior currently observed in the application.

The implementation uses:
- JUnit 5 for test execution and assertions
- Mockito for `StatisticsService` unit tests
- MockMvc for `StatisticsController` HTTP-level API tests

The tests will validate the response contract for `StatisticsResponseDTO` fields:
- `id`
- `value`
- `label`

The current implementation returns a JSON array of DTOs from `GET /api/v1/statistics` and preserves the service list order. When the repository returns an empty list, the controller still responds with `200 OK` and `[]`.

## Technical Context

**Language/Version**: Java 25

**Primary Dependencies**: Spring Boot 4.1.1, Spring WebMVC, Spring Validation, Spring Security, Spring Data JPA

**Storage**: PostgreSQL via Spring Data JPA configuration; this feature tests the list retrieval contract and not persistence implementation details

**Testing**: JUnit 5, Mockito, MockMvc, Spring Boot test starter

**Target Platform**: Backend REST API service

**Project Type**: Web service

**Performance Goals**: Not a performance focus for this feature; validation is behavior- and contract-focused

**Constraints**:
- No production code modification
- No POST, PUT, PATCH, or DELETE tests
- Only `GET /api/v1/statistics` behavior is in scope
- Current application behavior for an empty repository result is a successful `200 OK` with an empty array

**Scale/Scope**: Small API test effort focused on a single controller and service contract

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

The project constitution file at `.specify/memory/constitution.md` is still a template and does not define project-specific governance, so there are no active technological or process gates to enforce for this test-only feature. This plan remains aligned with the requirement to avoid production code changes and to focus only on the Statistics GET flow.

## Project Structure

### Documentation (this feature)

```text
specs/002-statistics-get-api-tests/
├── spec.md
├── plan.md
└── tasks.md   # to be created later via /speckit-tasks, not part of this planning step
```

### Source Code (repository root)

```text
src/
├── main/java/com/durrmi/backend/
│   ├── statistics/
│   │   ├── controller/StatisticsController.java
│   │   ├── dto/StatisticsRequestDTO.java
│   │   ├── dto/StatisticsResponseDTO.java
│   │   ├── entity/Statistics.java
│   │   ├── repository/StatisticsRepository.java
│   │   └── service/StatisticsService.java
│   └── ...
└── test/java/com/durrmi/backend/
    └── statistics/
        ├── controller/StatisticsControllerTest.java
        └── service/StatisticsServiceTest.java
```

**Structure Decision**: Keep the feature test work within the existing Spring Boot package layout, adding test classes under the corresponding `statistics` package under `src/test/java`. No new production packages or APIs are required.

## Test Strategy

### Phase 1: Service-layer unit tests

Purpose: validate the business behavior of `StatisticsService.getAllStatistics()` in isolation using Mockito.

Planned coverage:
1. Statistics records exist
   - Mock `StatisticsRepository.findAll()` to return a list of `Statistics` entities
   - Call `statisticsService.getAllStatistics()`
   - Assert the returned list is not null
   - Assert size matches the mocked list size
   - Assert each returned `StatisticsResponseDTO` has the expected `id`, `value`, and `label`
   - Assert order is preserved in the returned list

2. Statistics records are empty
   - Mock `StatisticsRepository.findAll()` to return `Collections.emptyList()`
   - Call `statisticsService.getAllStatistics()`
   - Assert the returned list is empty
   - Confirm no exception is thrown in the current application behavior

Test class target:
- `src/test/java/com/durrmi/backend/statistics/service/StatisticsServiceTest.java`

### Phase 2: Controller/API tests with MockMvc

Purpose: validate the HTTP contract exposed by `StatisticsController.getAllStatistics()` without changing production code.

Planned coverage:
1. Statistics list exists
   - Mock `StatisticsService.getAllStatistics()` to return a list of `StatisticsResponseDTO` objects
   - Send `GET /api/v1/statistics`
   - Assert HTTP status is `200 OK`
   - Assert the response is a JSON array
   - Assert the array size matches the expected number of records
   - Assert order is preserved across the JSON list
   - Assert each object has `id`, `value`, and `label` with expected values

2. Empty statistics list
   - Mock `StatisticsService.getAllStatistics()` to return an empty list
   - Send `GET /api/v1/statistics`
   - Assert HTTP status is `200 OK`
   - Assert response body is `[]`

Test class target:
- `src/test/java/com/durrmi/backend/statistics/controller/StatisticsControllerTest.java`

## Scope and Exclusions

In-scope:
- `GET /api/v1/statistics`
- multiple statistics records scenario
- empty-list scenario
- list ordering and size verification
- `StatisticsResponseDTO` field validation
- JUnit 5 + Mockito + MockMvc testing

Out of scope:
- POST, PUT, PATCH, or DELETE endpoints
- production code changes
- database integration tests beyond the mocked repository behavior
- any non-Statistics API or helper logic

## Validation Approach

The implementation will be considered complete when the test suite confirms:
- service success path returns a list of DTOs with the correct values
- service empty-list path returns an empty list
- controller success path responds with `200 OK` and correct JSON list payload
- controller empty-list path responds with `200 OK` and `[]`
- list size and ordering are asserted on the JSON array

## Complexity Tracking

No constitution-based exceptions or simplification tradeoffs are required for this feature because the plan remains within the existing Spring Boot architecture and the established behavior of the current application.
