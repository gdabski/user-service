package gdabski.demo.user.it;

import static gdabski.demo.user.it.matcher.HttpStatusMatcher.is;
import static gdabski.demo.user.it.service.UserServiceMethods.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.inject.Inject;

import gdabski.demo.user.it.conf.ITTest;
import gdabski.demo.user.it.service.UserServiceMethods;
import org.junit.jupiter.api.Test;

@ITTest
public class DeleteUserIT {

    @Inject
    private UserServiceMethods userService;

    @Test
    public void shouldRemoveAndReturnNoContent_when_deletingOwnUserProfileAdminAuthenticated() {
        // given
        var postAdminResponse = userService.postUser(defaultAdminSpecification());
        var postUserResponse = userService.postUser(defaultUserSpecification());

        // when
        var response = userService.newAdminAuthenticatedRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .delete(users(postAdminResponse.path("id")));

        // then
        response.then()
                .statusCode(is(NO_CONTENT));

        userService.getUser(postAdminResponse.path("id")).then()
                .statusCode(is(NOT_FOUND));

        userService.getUser(postUserResponse.path("id")).then()
                .statusCode(is(OK));
    }

    @Test
    public void shouldRemoveAndReturnNoContent_when_deletingOtherUserProfileAdminAuthenticated() {
        // given
        var postAdminResponse = userService.postUser(defaultAdminSpecification());
        var postOtherUserResponse = userService.postUser(repeatableUserSpecification("usertwo"));
        userService.postUser(defaultUserSpecification());

        // when
        var response = userService.newAdminAuthenticatedRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .delete(users(postOtherUserResponse.path("id")));

        // then
        response.then()
                .statusCode(is(NO_CONTENT));

        userService.getUser(postOtherUserResponse.path("id")).then()
                .statusCode(is(NOT_FOUND));

        userService.getUser(postAdminResponse.path("id")).then()
                .statusCode(is(OK));
    }

    @Test
    public void shouldRemoveAndReturnNoContent_when_deletingOwnUserProfileUserAuthenticated() {
        // given
        var otherUserSpecification = repeatableUserSpecification("usertwo");

        var postDefaultUserResponse = userService.postUser(defaultUserSpecification());
        var postOtherUserResponse = userService.postUser(otherUserSpecification);

        // when
        var response = userService.newRequest()
                .auth().preemptive().basic(otherUserSpecification.getUsername(), otherUserSpecification.getPassword())
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .delete(users(postOtherUserResponse.path("id")));

        // then
        response.then()
                .statusCode(is(NO_CONTENT));

        userService.getUser(postOtherUserResponse.path("id")).then()
                .statusCode(is(NOT_FOUND));

        userService.getUser(postDefaultUserResponse.path("id")).then()
                .statusCode(is(OK));
    }

    @Test
    public void shouldReturnForbidden_when_deletingOtherUserProfileUserAuthenticated() {
        // given
        var postAdminResponse = userService.postUser(defaultAdminSpecification());
        userService.postUser(defaultUserSpecification());

        // when
        var response = userService.newUserAuthenticatedRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .delete(users(postAdminResponse.path("id")));

        // then
        response.then()
                .statusCode(is(FORBIDDEN));

        userService.getUser(postAdminResponse.path("id")).then()
                .statusCode(is(OK));
    }

    @Test
    public void shouldReturnNotFound_when_userProfileMissing() {
        // given
        userService.postUser(defaultAdminSpecification());

        // when
        var response = userService.newAdminAuthenticatedRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .delete(users(Integer.MAX_VALUE));

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
                .delete(users(postUserResponse.path("id")));

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
                .delete(users(postUserResponse.path("id")));

        // then
        response.then()
                .statusCode(is(UNAUTHORIZED));
    }

}
