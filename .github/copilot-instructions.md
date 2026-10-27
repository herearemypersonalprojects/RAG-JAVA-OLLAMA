
# Project Engineering Instructions

## Technical Stack
- Java 21
- Maven
- Spring Boot 3.x or later compatible version
- JUnit 5
- Mockito
- Hexagonal Architecture (Ports and Adapters)

## Architecture Rules
- Domain must not depend on Spring.
- Domain must not depend on infrastructure.
- Application defines use cases and ports.
- Infrastructure implements technical adapters.
- REST controllers delegate to application use cases.
- Persistence entities must not be exposed as REST DTOs.
- Business rules belong in the domain or application layer.
- Dependencies must point inward.

## Java Development Rules
- Follow SOLID principles.
- Prefer constructor injection.
- Avoid field injection.
- Prefer immutable objects when appropriate.
- Use Java records for suitable DTOs.
- Use clear business-oriented names.
- Avoid unnecessary abstractions.
- Do not add dependencies without justification.
- Avoid logging sensitive information.

## Testing Rules
- Use JUnit 5 and Mockito for unit tests.
- Use integration tests for Spring or persistence behavior.
- Test happy paths, boundaries and error cases.
- Do not mock domain objects unnecessarily.
- Never modify production logic only to make a faulty test pass.
- Run Maven verification when tools permit.
- Never claim tests passed unless they were executed.

## Quality Rules
- Review error handling.
- Review thread safety where relevant.
- Review security and input validation.
- Flag violations of hexagonal architecture.
- Report compilation errors and failing tests.
- Do not claim generated code is production-ready without verification.
