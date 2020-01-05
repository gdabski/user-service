package gdabski.demo.user.it;

import static gdabski.demo.user.it.matcher.HttpStatusMatcher.is;
import static gdabski.demo.user.it.service.UserServiceMethods.*;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.inject.Inject;

import gdabski.demo.user.it.conf.ITTest;
import gdabski.demo.user.it.service.UserServiceMethods;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

@ITTest
public class GetUserIT {

    @Inject
    private UserServiceMethods userService;

    @Test
    public void shouldReturnOkWithUserSummary_when_gettingOwnUserProfileUserAuthenticated() {
        // given
        var postUserResponse = userService.postUser(defaultUserSpecification());

        // when
        var response = userService.newUserAuthenticatedRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .get(users(postUserResponse.path("id")));

        // then
        response.then()
                .statusCode(is(OK))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body(equalTo(postUserResponse.getBody().asString()));
    }

    @Test
    public void shouldReturnOkWithUserSummary_when_gettingOtherUserProfileUserAuthenticated() {
        // given
        userService.postUser(defaultUserSpecification());
        Response postAdminResponse = userService.postUser(defaultAdminSpecification());

        // when
        var response = userService.newUserAuthenticatedRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .get(users(postAdminResponse.path("id")));

        // then
        response.then()
                .statusCode(is(OK))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body(equalTo(postAdminResponse.getBody().asString()));
    }

    @Test
    public void shouldReturnOkWithUserSummary_when_gettingOwnUserProfileAdminAuthenticated() {
        // given
        var postAdminResponse = userService.postUser(defaultAdminSpecification());

        // when
        var response = userService.newAdminAuthenticatedRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .get(users(postAdminResponse.path("id")));

        // then
        response.then()
                .statusCode(is(OK))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body(equalTo(postAdminResponse.getBody().asString()));
    }

    @Test
    public void shouldReturnOkWithUserSummary_when_gettingOtherUserProfileAdminAuthenticated() {
        // given
        userService.postUser(defaultAdminSpecification());
        Response postUserResponse = userService.postUser(defaultUserSpecification());

        // when
        var response = userService.newAdminAuthenticatedRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .get(users(postUserResponse.path("id")));

        // then
        response.then()
                .statusCode(is(OK))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body(equalTo(postUserResponse.getBody().asString()));
    }

    @Test
    public void shouldReturnNotFound_when_userProfileMissing() {
        // given
        userService.postUser(defaultAdminSpecification());

        // when
        var response = userService.newAdminAuthenticatedRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .get(users(Integer.MAX_VALUE));

        // then
        response.then()
                .statusCode(is(NOT_FOUND));
    }

    @Test
    public void shouldReturnNotAcceptable_when_apiVersionNotProvided() {
        // given
        var postUserResponse = userService.postUser(defaultAdminSpecification());

        // when
        var response = userService.newAdminAuthenticatedRequest()
                .accept(APPLICATION_JSON_VALUE)
                .get(users(postUserResponse.path("id")));

        // then
        response.then()
                .statusCode(is(NOT_ACCEPTABLE));
    }

    @Test
    public void shouldReturnUnauthorized_when_notAuthenticated() {
        // given
        var postUserResponse = userService.postUser(defaultAdminSpecification());

        // when
        var response = userService.newRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .get(users(postUserResponse.path("id")));

        // then
        response.then()
                .statusCode(is(UNAUTHORIZED));
    }

}
