package gdabski.demo.user.it.service;

import static io.restassured.config.LogConfig.logConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;
import static java.util.Collections.singleton;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Set;

import gdabski.demo.user.it.fixture.ITUserSpecification;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("it")
@Lazy   // this allows getting the random "local.server.port" property the easy way
public class UserServiceMethods {

    private final static RestAssuredConfig CONFIG = newConfig()
            .logConfig(logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

    public static final String APPLICATION_GDABSKI_DEMO_USER_V1 = "application/vnd.gdabski.demo.user.v1+json";

    private static final ITUserSpecification DEFAULT_ADMIN_SPECIFICATION = buildDefaultAdminSpecification();
    private static final ITUserSpecification DEFAULT_USER_SPECIFICATION = buildDefaultUserSpecification();

    @LocalServerPort
    private int serverPort;

    /**
     * Performs a valid POST request to the {@link #users()} endpoint.
     * @return the {@link Response} to the request
     */
    public final Response postUser(ITUserSpecification userSpecification) {
        return newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification)
                .post(users());
    }

    /**
     * Performs a valid user authenticated GET request to the {@link #users(int)}
     * endpoint.
     * @return the {@link Response} to the request
     */
    public final Response getUser(int id) {
        return newUserAuthenticatedRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .get(users(id));
    }

    /**
     * To be used instead of {@link RestAssured#given()} to begin building a
     * request specification with a custom {@link RestAssuredConfig} as well
     * as the server port configured and basic authentication set for the
     * {@link #defaultAdminSpecification() default admin user}.
     * @return {@link RequestSpecification} with some defaults and admin
     * authentication configured
     */
    public final RequestSpecification newAdminAuthenticatedRequest() {
        return newRequest().auth().preemptive().basic(
                defaultAdminSpecification().getUsername(),
                defaultAdminSpecification().getPassword()
        );
    }

    /**
     * To be used instead of {@link RestAssured#given()} to begin building a
     * request specification with a custom {@link RestAssuredConfig} as well
     * as the server port configured and basic authentication set for the
     * {@link #defaultUserSpecification() default user}.
     * @return {@link RequestSpecification} with some defaults and user
     * authentication configured
     */
    public final RequestSpecification newUserAuthenticatedRequest() {
        return newRequest().auth().preemptive().basic(
                defaultUserSpecification().getUsername(),
                defaultUserSpecification().getPassword()
        );
    }

    /**
     * To be used instead of {@link RestAssured#given()} to begin building a
     * request specification with a custom {@link RestAssuredConfig} as well
     * as the server port configured.
     * @return {@link RequestSpecification} with some defaults configured
     */
    public final RequestSpecification newRequest() {
        return RestAssured.given().config(CONFIG).port(serverPort).auth().none();
    }

    /**
     * @return A corrupt JSON string.
     */
    public static String corruptJson() {
        return "{username: \"charlie\"}";
    }


    /**
     * @return path representing the /users endpoint
     */
    public static String users() {
        return "/users";
    }

    /**
     * @param id the ID
     * @return path representing the /users/{id} endpoint
     */
    public static String users(int id) {
        return String.format("/users/%d", id);
    }

    /**
     * @param username the username for the user
     * @return A ready-made {@link ITUserSpecification} of a user
     */
    public static ITUserSpecification repeatableUserSpecification(String username) {
        return ITUserSpecification.defaultBuilder()
                .username(username)
                .password("pa$$w0rd")
                .name(String.format("%s %s", username, username))
                .email(String.format("%s@example.com", username))
                .roles(singleton("USER"))
                .build();
    }

    /**
     * @return A ready-made {@link ITUserSpecification} of an admin user
     */
    public static ITUserSpecification defaultAdminSpecification() {
        return DEFAULT_ADMIN_SPECIFICATION;
    }

    /**
     * @return A ready-made {@link ITUserSpecification} of a user
     */
    public static ITUserSpecification defaultUserSpecification() {
        return DEFAULT_USER_SPECIFICATION;
    }

    private static ITUserSpecification buildDefaultAdminSpecification() {
        return ITUserSpecification.defaultBuilder()
                .username("admin")
                .password("admin")
                .name("Admin Admin")
                .email("admin@example.int")
                .roles(Set.of("ADMIN"))
                .build();
    }

    private static ITUserSpecification buildDefaultUserSpecification() {
        return ITUserSpecification.defaultBuilder()
                .username("joe123")
                .password("pa$$w0rd")
                .name("User User")
                .email("user@example.int")
                .roles(Set.of("USER"))
                .build();
    }

}
