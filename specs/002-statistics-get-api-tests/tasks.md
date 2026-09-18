# Tasks: Statistics GET API test coverage

**Input**: Design documents from `/specs/002-statistics-get-api-tests/`

**Prerequisites**: plan.md (required), spec.md (required for user stories)

**Tests**: This feature explicitly requests automated tests; these tasks cover only the Statistics GET API scenarios and omit POST, PUT, PATCH, and DELETE coverage.

**Organization**: Tasks are grouped by the Statistics retrieval scenarios so they can be implemented and validated independently.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Ensure the test environment is ready for Statistics GET API coverage.

- [ ] T001 Configure test class structure under `src/test/java/com/durrmi/backend/statistics/service/` and `src/test/java/com/durrmi/backend/statistics/controller/`
- [ ] T002 Verify the project already includes JUnit 5, Mockito, and MockMvc support via Spring Boot test dependencies
- [ ] T003 [P] Confirm the test naming conventions and package layout match the existing Spring Boot project structure

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Establish the shared knowledge needed before writing Statistics GET API test cases.

- [ ] T004 Review `StatisticsService.getAllStatistics()` behavior and confirm the list-based response contract
- [ ] T005 Review `StatisticsController.getAllStatistics()` behavior and confirm the current controller response flow for success and empty-list cases
- [ ] T006 Confirm `StatisticsResponseDTO` field contract: `id`, `value`, and `label`
- [ ] T007 [P] Confirm no production code changes are allowed for this task set and only GET tests are in scope

**Checkpoint**: Foundation ready - Statistics GET API test implementation can now begin.

---

## Phase 3: User Story 1 - Fetch all statistics cards when records exist (Priority: P1) 🎯 MVP

**Goal**: Verify the service and controller return the correct Statistics GET response when records are available.

**Independent Test**: A service call and an HTTP GET request can both be validated independently against the expected list of statistics cards and their field values.

### Tests for User Story 1 ⚠️

- [ ] T008 [P] [US1] Create `StatisticsServiceTest` for the multiple-record success scenario in `src/test/java/com/durrmi/backend/statistics/service/StatisticsServiceTest.java`
- [ ] T009 [P] [US1] Create `StatisticsControllerTest` for the multiple-record success scenario in `src/test/java/com/durrmi/backend/statistics/controller/StatisticsControllerTest.java`

### Implementation for User Story 1

- [ ] T010 [US1] Mock `StatisticsRepository.findAll()` to return multiple `Statistics` entities in `src/test/java/com/durrmi/backend/statistics/service/StatisticsServiceTest.java`
- [ ] T011 [US1] Assert `StatisticsService.getAllStatistics()` returns a list of `StatisticsResponseDTO` objects with the correct `id`, `value`, and `label` values in `src/test/java/com/durrmi/backend/statistics/service/StatisticsServiceTest.java`
- [ ] T012 [US1] Assert the returned list size matches the mocked repository result size in `src/test/java/com/durrmi/backend/statistics/service/StatisticsServiceTest.java`
- [ ] T013 [US1] Assert the order of the returned list matches the repository order in `src/test/java/com/durrmi/backend/statistics/service/StatisticsServiceTest.java`
- [ ] T014 [US1] Mock `StatisticsService.getAllStatistics()` to return a list of `StatisticsResponseDTO` values in `src/test/java/com/durrmi/backend/statistics/controller/StatisticsControllerTest.java`
- [ ] T015 [US1] Send `GET /api/v1/statistics` with MockMvc and assert HTTP status is `200 OK` in `src/test/java/com/durrmi/backend/statistics/controller/StatisticsControllerTest.java`
- [ ] T016 [US1] Assert the JSON response is an array and verify the size, ordering, and field values for multiple statistics cards in `src/test/java/com/durrmi/backend/statistics/controller/StatisticsControllerTest.java`

**Checkpoint**: The multiple-record success path is validated at both the service and API layer.

---

## Phase 4: User Story 2 - Handle empty statistics list (Priority: P1)

**Goal**: Verify the current application behavior when the repository returns no statistics records.

**Independent Test**: The service and controller can each be validated independently for the empty-list path, confirming the current `200 OK` with empty array behavior.

### Tests for User Story 2 ⚠️

- [ ] T017 [P] [US2] Add the empty-list service test in `src/test/java/com/durrmi/backend/statistics/service/StatisticsServiceTest.java`
- [ ] T018 [P] [US2] Add the empty-list controller/API test in `src/test/java/com/durrmi/backend/statistics/controller/StatisticsControllerTest.java`

### Implementation for User Story 2

- [ ] T019 [US2] Mock `StatisticsRepository.findAll()` to return an empty list in `src/test/java/com/durrmi/backend/statistics/service/StatisticsServiceTest.java`
- [ ] T020 [US2] Assert `StatisticsService.getAllStatistics()` returns an empty list in `src/test/java/com/durrmi/backend/statistics/service/StatisticsServiceTest.java`
- [ ] T021 [US2] Mock `StatisticsService.getAllStatistics()` to return an empty list in `src/test/java/com/durrmi/backend/statistics/controller/StatisticsControllerTest.java`
- [ ] T022 [US2] Send `GET /api/v1/statistics` with MockMvc and assert HTTP status is `200 OK` in `src/test/java/com/durrmi/backend/statistics/controller/StatisticsControllerTest.java`
- [ ] T023 [US2] Assert the response body is an empty JSON array (`[]`) in `src/test/java/com/durrmi/backend/statistics/controller/StatisticsControllerTest.java`

**Checkpoint**: The empty-list path is validated according to the existing application behavior.

---

## Phase 5: Validation & Verification

**Purpose**: Run the targeted Statistics GET test suite and verify all required scenarios pass.

- [ ] T024 Run the Statistics service test class and confirm the multiple-record and empty-list scenarios pass
- [ ] T025 Run the Statistics controller test class and confirm HTTP status and JSON-array assertions pass
- [ ] T026 Verify all tests covering `id`, `value`, and `label` pass for the Statistics GET API
- [ ] T027 Confirm the test scope remains limited to Statistics GET behavior and excludes POST, PUT, PATCH, and DELETE tests
- [ ] T028 Run the targeted Maven test command for the Statistics tests and confirm the suite succeeds without modifying production code

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion
- **User Story 1 (Phase 3)**: Depends on Foundational completion
- **User Story 2 (Phase 4)**: Depends on Foundational completion
- **Validation (Phase 5)**: Depends on all Statistics GET test tasks being complete

### Parallel Opportunities

- T003 can run in parallel with other setup tasks
- T008 and T009 can run in parallel because they cover different files
- T017 and T018 can run in parallel because they cover different files
- T024 and T025 can be run in parallel only if the test runner is configured to run separate classes concurrently, otherwise follow the sequential Maven test execution path

### Within Each User Story

- Service tests should be written and validated before controller tests for the same scenario
- The empty-list scenario should validate the actual current application behavior: `200 OK` with `[]`
- The implementation must remain strictly limited to test code for the Statistics GET API

## Implementation Strategy

### MVP First

1. Complete Phase 1 and Phase 2
2. Implement the multiple-record success tests
3. Validate success path
4. Implement empty-list tests
5. Validate failure/empty path
6. Run targeted test suite and confirm the Statistics GET API behavior is proven

### Incremental Delivery

- Story 1 delivers the success path and list contract validation
- Story 2 delivers the empty-list behavior using the current application behavior
- Validation phase confirms both scenarios and the required field-level assertions

## Notes

- The tasks intentionally exclude any POST, PUT, PATCH, or DELETE work
- No production code modifications are planned or allowed
- Task descriptions include exact file paths in the expected test locations
- The feature remains focused on Statistics GET API automated tests only
