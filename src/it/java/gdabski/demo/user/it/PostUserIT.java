package gdabski.demo.user.it;

import static com.google.common.collect.Sets.newHashSet;
import static gdabski.demo.user.it.matcher.HttpStatusMatcher.is;
import static gdabski.demo.user.it.service.UserServiceMethods.*;
import static java.util.Collections.emptySet;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.inject.Inject;
import java.util.Set;

import com.google.common.base.Strings;
import gdabski.demo.user.it.conf.ITTest;
import gdabski.demo.user.it.fixture.ITUserSpecification;
import gdabski.demo.user.it.service.UserServiceMethods;
import org.junit.jupiter.api.Test;

@ITTest
class PostUserIT {

    @Inject
    private UserServiceMethods userService;

    @Test
    public void shouldReturnCreated_when_validUserSpecificationPosted() {
        // given
        var userSpecification = ITUserSpecification.defaultBuilder()
                .roles(Set.of("ADMIN", "USER")) // elsewhere a single role will be provided
                .build();

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification).post(users());

        // then
        response.then()
                .statusCode(is(CREATED))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .header(LOCATION, users(response.path("id")))
                .body("id", instanceOf(Integer.class))
                .body("username", equalTo(userSpecification.getUsername()))
                .body("name", equalTo(userSpecification.getName()))
                .body("roles", containsInAnyOrder(userSpecification.getRoles().toArray()))
                .body("email", equalTo(userSpecification.getEmail()))
                .body("state", equalTo(userSpecification.getState()))
                .body("comment", equalTo(userSpecification.getComment()))
                .body("$", not(hasKey("password")));
    }

    @Test
    public void shouldReturnCreated_when_validUserSpecificationWithoutOptionalFieldsPosted() {
        // given
        var userSpecification = ITUserSpecification.defaultBuilder()
                .name(null)
                .email(null)
                .comment(null)
                .build();

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification).post(users());

        // then
        response.then()
                .statusCode(is(CREATED))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .header(LOCATION, users(response.path("id")))
                .body("id", instanceOf(Integer.class))
                .body("username", equalTo(userSpecification.getUsername()))
                .body("name", equalTo(userSpecification.getName()))
                .body("roles", containsInAnyOrder(userSpecification.getRoles().toArray()))
                .body("email", equalTo(userSpecification.getEmail()))
                .body("state", equalTo(userSpecification.getState()))
                .body("comment", equalTo(userSpecification.getComment()))
                .body("$", not(hasKey("password")));
    }

    @Test
    public void shouldReturnUnsupportedMediaType_when_contentTypeNotProvided() {
        // given
        var userSpecification = ITUserSpecification.defaultBuilder().build();

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body(userSpecification).post(users());

        // then
        response.then()
                .statusCode(is(UNSUPPORTED_MEDIA_TYPE));
    }

    @Test
    public void shouldReturnNotAcceptable_when_apiVersionNotProvided() {
        // given
        var userSpecification = ITUserSpecification.defaultBuilder().build();

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification).post(users());

        // then
        response.then()
                .statusCode(is(NOT_ACCEPTABLE));
    }

    @Test
    public void shouldReturnBadRequest_when_corruptJsonPosted() {
        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(corruptJson()).post(users());

        // then
        response.then()
                .statusCode(is(BAD_REQUEST))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("message", notNullValue())
                .body("path", equalTo(users()));
    }

    @Test
    public void shouldReturnBadRequest_when_emptyBodyPosted() {
        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE).post(users());

        // then
        response.then()
                .statusCode(is(BAD_REQUEST))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("message", notNullValue())
                .body("path", equalTo(users()));
    }

    @Test
    public void shouldReturnUnprocessableEntity_when_usernameContainsIllegalCharacters() {
        // given
        var userSpecification = ITUserSpecification.defaultBuilder()
                .username("joe123^^")
                .build();

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification).post(users());

        // then
        response.then()
                .statusCode(is(UNPROCESSABLE_ENTITY))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("message", notNullValue())
                .body("path", equalTo(users()));
    }

    @Test
    public void shouldReturnUnprocessableEntity_when_usernameTooLong() {
        // given
        var userSpecification = ITUserSpecification.defaultBuilder()
                .username(Strings.repeat("x", 17))
                .build();

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification).post(users());

        // then
        response.then()
                .statusCode(is(UNPROCESSABLE_ENTITY))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("message", notNullValue())
                .body("path", equalTo(users()));
    }

    @Test
    public void shouldReturnUnprocessableEntity_when_usernameNotProvided() {
        // given
        var userSpecification = ITUserSpecification.defaultBuilder()
                .username(null)
                .build();

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification).post(users());

        // then
        response.then()
                .statusCode(is(UNPROCESSABLE_ENTITY))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("message", notNullValue())
                .body("path", equalTo(users()));
    }

    @Test
    public void shouldReturnUnprocessableEntity_when_usernameDuplicate() {
        // given
        var userSpecification1 = ITUserSpecification.defaultBuilder()
                .username("joe123")
                .name("Joe the First")
                .build();

        userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification1).post(users());

        var userSpecification2 = ITUserSpecification.defaultBuilder()
                .username("joe123")
                .name("Joe the Second")
                .build();

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification2).post(users());

        // then
        response.then()
                .statusCode(is(CONFLICT))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("message", notNullValue())
                .body("path", equalTo(users()));
    }

    @Test
    public void shouldReturnUnprocessableEntity_when_passwordTooLong() {
        // given
        var userSpecification = ITUserSpecification.defaultBuilder()
                .password(Strings.repeat("x", 1025))
                .build();

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification).post(users());

        // then
        response.then()
                .statusCode(is(UNPROCESSABLE_ENTITY))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("message", notNullValue())
                .body("path", equalTo(users()));
    }

    @Test
    public void shouldReturnUnprocessableEntity_when_passwordNotProvided() {
        // given
        var userSpecification = ITUserSpecification.defaultBuilder()
                .password(null)
                .build();

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification).post(users());

        // then
        response.then()
                .statusCode(is(UNPROCESSABLE_ENTITY))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("message", notNullValue())
                .body("path", equalTo(users()));
    }

    @Test
    public void shouldReturnUnprocessableEntity_when_nameTooLong() {
        // given
        var userSpecification = ITUserSpecification.defaultBuilder()
                .name(Strings.repeat("x", 65))
                .build();

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification).post(users());

        // then
        response.then()
                .statusCode(is(UNPROCESSABLE_ENTITY))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("message", notNullValue())
                .body("path", equalTo(users()));
    }

    @Test
    public void shouldReturnUnprocessableEntity_when_rolesEmpty() {
        // given
        var userSpecification = ITUserSpecification.defaultBuilder()
                .roles(emptySet())
                .build();

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification).post(users());

        // then
        response.then()
                .statusCode(is(UNPROCESSABLE_ENTITY))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("message", notNullValue())
                .body("path", equalTo(users()));
    }

    @Test
    public void shouldReturnUnprocessableEntity_when_rolesContainNullElements() {
        // given
        var userSpecification = ITUserSpecification.defaultBuilder()
                .roles(newHashSet("ADMIN", null))
                .build();

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification).post(users());

        // then
        response.then()
                .statusCode(is(UNPROCESSABLE_ENTITY))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("message", notNullValue())
                .body("path", equalTo(users()));
    }

    @Test
    public void shouldReturnUnprocessableEntity_when_rolesNotProvided() {
        // given
        var userSpecification = ITUserSpecification.defaultBuilder()
                .roles(null)
                .build();

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification).post(users());

        // then
        response.then()
                .statusCode(is(UNPROCESSABLE_ENTITY))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("message", notNullValue())
                .body("path", equalTo(users()));
    }

    @Test
    public void shouldReturnUnprocessableEntity_when_emailInvalid() {
        // given
        var userSpecification = ITUserSpecification.defaultBuilder()
                .email("joe123(at)example.com")
                .build();

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification).post(users());

        // then
        response.then()
                .statusCode(is(UNPROCESSABLE_ENTITY))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("message", notNullValue())
                .body("path", equalTo(users()));
    }

    @Test
    public void shouldReturnUnprocessableEntity_when_stateNotProvided() {
        // given
        var userSpecification = ITUserSpecification.defaultBuilder()
                .state(null)
                .build();

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification).post(users());

        // then
        response.then()
                .statusCode(is(UNPROCESSABLE_ENTITY))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("message", notNullValue())
                .body("path", equalTo(users()));
    }

    @Test
    public void shouldReturnForbidden_when_authenticated() {
        // given
        userService.postUser(defaultAdminSpecification());
        var userSpecification = ITUserSpecification.defaultBuilder().build();

        // when
        var response = userService.newAdminAuthenticatedRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userSpecification).post(users());

        // then
        response.then()
                .statusCode(is(FORBIDDEN));
    }

}