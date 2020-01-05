package gdabski.demo.user.it;

import static gdabski.demo.user.it.matcher.HttpStatusMatcher.is;
import static gdabski.demo.user.it.service.UserServiceMethods.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.inject.Inject;

import gdabski.demo.user.it.conf.ITTest;
import gdabski.demo.user.it.fixture.ITUserPatch;
import gdabski.demo.user.it.service.UserServiceMethods;
import org.junit.jupiter.api.Test;

@ITTest
public class PatchUserIT {

    @Inject
    private UserServiceMethods userService;

    @Test
    public void shouldUpdateAndReturnOkWithSummary_when_patchingOwnUserProfileUserAuthenticated() {
        // given
        var userSpecification = defaultUserSpecification();
        var userPatch = ITUserPatch.defaultBuilder()
                .comment(null)
                .build();

        var postUserResponse = userService.postUser(userSpecification);

        // when
        var response = userService.newUserAuthenticatedRequest()
                .accept(APPLICATION_GDABSKI_DEMO_USER_V1)
                .contentType(APPLICATION_JSON_VALUE)
                .body(userPatch)
                .patch(users(postUserResponse.path("id")));

        // then
        response.then()
                .statusCode(is(OK))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("id", equalTo(postUserResponse.path("id")))
                .body("username", equalTo(userSpecification.getUsername()))
                .body("name", equalTo(userPatch.getName()))
                .body("roles", containsInAnyOrder(userPatch.getRoles().toArray()))
                .body("email", equalTo(userPatch.getEmail()))
                .body("state", equalTo(userPatch.getState()))
                .body("comment", equalTo(userSpecification.getComment()));

        userService.getUser(postUserResponse.path("id")).then()
                .statusCode(is(OK))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("id", equalTo(postUserResponse.path("id")))
                .body("username", equalTo(userSpecification.getUsername()))
                .body("name", equalTo(userPatch.getName()))
                .body("roles", containsInAnyOrder(userPatch.getRoles().toArray()))
                .body("email", equalTo(userPatch.getEmail()))
                .body("state", equalTo(userPatch.getState()))
                .body("comment", equalTo(userSpecification.getComment()));
    }

    // more tests should follow

}
