# Unit and Integration Tests in Spring Boot using JUnit 5, Mockito & Testcontainers

In this project I implemented **unit and integration tests** for **repository, service and controller layers**.

## What I Built & Learned

From this project I learned how to:

* **Implement unit tests and integration tests** using **Behaviour-Driven Development (BDD)** with the format below:
```java
@DisplayName("")
@Test
public void given_when_then() {
    //given -> precondition/setup
    
    //when -> action that we are going to test
    
    //then -> verify the output
}
```

* **Write unit tests** for individual layers:
  - **Repository layer** - Data access testing
  - **Service layer** - Business logic testing
  - **Controller layer** - REST API endpoint testing

* **Carry out integration tests** to cover the entire application layers end-to-end

* **Implement integration tests** using **Testcontainers** with PostgreSQL

* **Use Spring Boot testing annotations** effectively:
  - `@SpringBootTest` - Full application context
  - `@WebMvcTest` - Controller layer testing
  - `@DataJpaTest` - Repository layer testing
  - `@MockBean` - Mocking beans in Spring context

* **Create mock objects and stubs** to simulate external dependencies and isolate units under test

* **Write integration tests** using **PostgreSQL** for production-like testing environment

## Tools and Technologies

**Core Framework:**
- Spring Boot
- Spring MVC (REST APIs)
- Spring Data JPA
- Hibernate/JPA

**Testing Frameworks:**
- JUnit 5
- Mockito
- MockMvc
- Testcontainers

**Utilities:**
- AssertJ
- Hamcrest
- Jackson
- Lombok
