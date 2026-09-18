# Tasks: Hero GET API test coverage

**Input**: Design documents from `/specs/001-hero-get-api-tests/`

**Prerequisites**: plan.md (required), spec.md (required for user stories)

**Tests**: This feature explicitly requests automated tests; these tasks cover only the Hero GET API scenarios and omit POST, PUT, PATCH, and DELETE coverage.

**Organization**: Tasks are grouped by the Hero retrieval scenarios so they can be implemented and validated independently.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Ensure the test environment is ready for Hero GET API coverage.

- [ ] T001 Configure test class structure under `src/test/java/com/durrmi/backend/hero/service/` and `src/test/java/com/durrmi/backend/hero/controller/`
- [ ] T002 Verify the project already includes JUnit 5, Mockito, and MockMvc support via Spring Boot test dependencies
- [ ] T003 [P] Confirm the test naming conventions and package layout match the existing Spring Boot project structure

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Establish the shared knowledge needed before writing Hero GET API test cases.

- [ ] T004 Review `HeroService.getHero()` behavior and confirm the missing ID 1 failure contract (`RuntimeException("Hero not found")`)
- [ ] T005 Review `HeroController.getHero()` behavior and confirm the current controller response flow for success and failure
- [ ] T006 Confirm `HeroResponseDTO` field contract: `id`, `title`, `description`, and `customerText`
- [ ] T007 [P] Confirm no production code changes are allowed for this task set and only GET tests are in scope

**Checkpoint**: Foundation ready - Hero GET API test implementation can now begin.

---

## Phase 3: User Story 1 - Hero retrieval succeeds when ID 1 exists (Priority: P1) 🎯 MVP

**Goal**: Verify the service and controller return the correct Hero GET response when the primary hero exists.

**Independent Test**: A service call and an HTTP GET request can both be validated independently against the known expected values for Hero ID 1.

### Tests for User Story 1 ⚠️

- [ ] T008 [P] [US1] Create `HeroServiceTest` for the successful Hero ID 1 scenario in `src/test/java/com/durrmi/backend/hero/service/HeroServiceTest.java`
- [ ] T009 [P] [US1] Create `HeroControllerTest` for the successful Hero ID 1 scenario in `src/test/java/com/durrmi/backend/hero/controller/HeroControllerTest.java`

### Implementation for User Story 1

- [ ] T010 [US1] Mock `HeroRepository.findById(1L)` to return a valid `Hero` entity in `src/test/java/com/durrmi/backend/hero/service/HeroServiceTest.java`
- [ ] T011 [US1] Assert `HeroService.getHero()` returns a `HeroResponseDTO` with the correct `id`, `title`, `description`, and `customerText` values in `src/test/java/com/durrmi/backend/hero/service/HeroServiceTest.java`
- [ ] T012 [US1] Mock `HeroService.getHero()` to return a `HeroResponseDTO` with expected values in `src/test/java/com/durrmi/backend/hero/controller/HeroControllerTest.java`
- [ ] T013 [US1] Send `GET /api/v1/hero` with MockMvc and assert HTTP status is `200 OK` in `src/test/java/com/durrmi/backend/hero/controller/HeroControllerTest.java`
- [ ] T014 [US1] Assert the JSON payload contains `id`, `title`, `description`, and `customerText` with the expected values in `src/test/java/com/durrmi/backend/hero/controller/HeroControllerTest.java`

**Checkpoint**: The Hero ID 1 success path is validated at both the service and API layer.

---

## Phase 4: User Story 2 - Hero retrieval fails when ID 1 is missing (Priority: P1)

**Goal**: Verify the current application behavior when the missing Hero ID 1 scenario occurs.

**Independent Test**: The service and controller can each be validated independently for the missing-hero path, asserting the current runtime-exception-based failure behavior.

### Tests for User Story 2 ⚠️

- [ ] T015 [P] [US2] Add the missing-Hero service test in `src/test/java/com/durrmi/backend/hero/service/HeroServiceTest.java`
- [ ] T016 [P] [US2] Add the missing-Hero controller/API test in `src/test/java/com/durrmi/backend/hero/controller/HeroControllerTest.java`

### Implementation for User Story 2

- [ ] T017 [US2] Mock `HeroRepository.findById(1L)` to return `Optional.empty()` in `src/test/java/com/durrmi/backend/hero/service/HeroServiceTest.java`
- [ ] T018 [US2] Assert `HeroService.getHero()` throws `RuntimeException` with message `"Hero not found"` in `src/test/java/com/durrmi/backend/hero/service/HeroServiceTest.java`
- [ ] T019 [US2] Mock `HeroService.getHero()` to throw `RuntimeException("Hero not found")` in `src/test/java/com/durrmi/backend/hero/controller/HeroControllerTest.java`
- [ ] T020 [US2] Send `GET /api/v1/hero` with MockMvc and assert HTTP status is `500 Internal Server Error` in `src/test/java/com/durrmi/backend/hero/controller/HeroControllerTest.java`
- [ ] T021 [US2] Confirm the missing-ID scenario does not return a successful `HeroResponseDTO` payload in `src/test/java/com/durrmi/backend/hero/controller/HeroControllerTest.java`

**Checkpoint**: The missing-Hero path is validated according to the existing application behavior.

---

## Phase 5: Validation & Verification

**Purpose**: Run the targeted Hero GET test suite and verify all required scenarios pass.

- [ ] T022 Run the Hero service test class and confirm the successful and missing-ID scenarios pass
- [ ] T023 Run the Hero controller test class and confirm HTTP status and JSON assertions pass
- [ ] T024 Verify all tests covering `id`, `title`, `description`, and `customerText` pass for the Hero GET API
- [ ] T025 Confirm the test scope remains limited to Hero GET behavior and excludes POST, PUT, PATCH, and DELETE tests
- [ ] T026 Run the targeted Maven test command for the Hero tests and confirm the suite succeeds without modifying production code

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion
- **User Story 1 (Phase 3)**: Depends on Foundational completion
- **User Story 2 (Phase 4)**: Depends on Foundational completion
- **Validation (Phase 5)**: Depends on all Hero GET test tasks being complete

### Parallel Opportunities

- T003 can run in parallel with other setup tasks
- T008 and T009 can run in parallel because they cover different files
- T015 and T016 can run in parallel because they cover different files
- T022 and T023 can be run in parallel only if the test runner is configured to run separate classes concurrently, otherwise follow the sequential Maven test execution path

### Within Each User Story

- Service tests should be written and validated before controller tests for the same scenario
- The missing-Hero scenario should use the current runtime-exception behavior described in the clarified specification
- The implementation must remain strictly limited to test code for the Hero GET API

## Implementation Strategy

### MVP First

1. Complete Phase 1 and Phase 2
2. Implement the successful Hero ID 1 service and controller tests
3. Validate success path
4. Implement missing-Hero service and controller tests
5. Validate failure path
6. Run targeted test suite and confirm the Hero GET API behavior is proven

### Incremental Delivery

- Story 1 delivers the success path and API contract validation
- Story 2 delivers the missing-resource failure path using the current application behavior
- Validation phase confirms both scenarios and the required field-level assertions

## Notes

- The tasks intentionally exclude any POST, PUT, PATCH, or DELETE work
- No production code modifications are planned or allowed
- Task descriptions include exact file paths in the expected test locations
- The feature remains focused on Hero GET API automated tests only
