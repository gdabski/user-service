# User Service

A demo REST microservice for managing users.

Per today's standards, making the application ready for quick and easy deployment would foremost require enclosing it in a software container (e.g. Docker) and preparing relevant deployment infrastructure. This might also involve implementing some liveness/heartbeat/registering on the side of this service or exposing management functions. Application properties can be overridden in production in the `java` command line, but it would also be possible to adapt the app to read them from the environment. The JAR could be slimmed down by excluding some unused dependencies introduced by the Spring Boot starters. API documentation could be created (e.g. Swagger).

The project demonstrates use of some basic security features based on own presumptions and ideas. Basic authentication is used without HTTPS/SSL exposing passwords in clear text to the network (not mentioning that a more advanced authentication scheme would likely be used in a production scenario). CSRF protection is disabled.

REST API versioning is achieved via content negotiation (Accept header). DTO names don't refer to the API versions, but that could be added if necessary. In case of an incompatible API change two approaches seem feasible: either one version DTO can be mapped to another version DTO (likely still in the controller layer) or all DTOs should be mapped to a common model, which would belong to the (currently mostly empty) domain package and be handled by the service layer. In case evolution of the RDBMS persistence model becomes necessary, I would look at tools such as Liquibase to facilitate.

The functional/integration tests provided are partially incomplete and many more cases could be added for the existing endpoints. It was a conscious decision not to do any testing on the controller using Spring's `@WebMvcTest` with the intent of assessing the behaviour of the application running in its entirety. In the past I found such a decision to be questioned as (admittedly) it does create additional programming effort.  

This project uses [Lombok](https://projectlombok.org/) and thus requires installing and/or configuring support in an IDE.


