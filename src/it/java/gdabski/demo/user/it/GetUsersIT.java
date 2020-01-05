package gdabski.demo.user.it;

import static gdabski.demo.user.it.matcher.HttpStatusMatcher.is;
import static gdabski.demo.user.it.service.UserServiceMethods.*;
import static java.util.stream.Collectors.toUnmodifiableMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.OK;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import gdabski.demo.user.it.conf.ITTest;
import gdabski.demo.user.it.fixture.ITUserSpecification;
import gdabski.demo.user.it.service.UserServiceMethods;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@ITTest
public class GetUsersIT {

    @Inject
    private UserServiceMethods userService;

    private Map<String, JsonPath> postUserResponseBodies;

    @BeforeEach
    public void setUp() {
        // given
        postUserResponseBodies = List.of(
                defaultUserSpecification(),
                ITUserSpecification.defaultBuilder().username("george1")
                        .name("George One").email("george@example.com").build(),
                ITUserSpecification.defaultBuilder().username("willy")
                        .name("Wilhelm Zwei").email("willy@mail.com").build(),
                ITUserSpecification.defaultBuilder().username("george2")
                        .name("George Two").email("george@gmail.com").build(),
                ITUserSpecification.defaultBuilder().username("magnificentwilly")
                        .name("Will One").email("willy@hotmail.com").build(),
                ITUserSpecification.defaultBuilder().username("georgie3")
                        .name("Three George").email("georgie3@example.com").build()
        ).stream()
        .map(userService::postUser)
        .map(response -> (ResponseBody<?>)response.body())
        .collect(toUnmodifiableMap(body -> body.path("username"), ResponseBody::jsonPath));
    }

    @Test
    public void shouldFilterAndReturnOkWithPage_when_gettingUsersByUsername() {
        // when
        var response = userService.newUserAuthenticatedRequest()
                .param("username", "georg")
                .get(users());

        // then
        response.then()
                .statusCode(is(OK))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("number", equalTo(0))
                .body("numberOfElements", equalTo(3))
                .body("totalElements", equalTo(3))
                .body("totalPages", equalTo(1));

        assertThat(response.jsonPath().getList("content"), containsInAnyOrder(
                postUserResponseBodies.get("george1").getMap("$"),
                postUserResponseBodies.get("george2").getMap("$"),
                postUserResponseBodies.get("georgie3").getMap("$")
        ));
    }

    @Test
    public void shouldFilterAndReturnOkWithPage_when_gettingUsersByAllFilterCriteriaIgnoringCase() {
        // when
        var response = userService.newUserAuthenticatedRequest()
                .param("username", "georg")
                .param("name", "george")
                .param("email", "GMAIL")
                .get(users());

        // then
        response.then()
                .statusCode(is(OK))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("number", equalTo(0))
                .body("numberOfElements", equalTo(1))
                .body("totalElements", equalTo(1))
                .body("totalPages", equalTo(1));

        assertThat(response.jsonPath().getList("content"), containsInAnyOrder(
                postUserResponseBodies.get("george2").getMap("$")
        ));
    }

    @Test
    public void shouldFilterAndReturnOkWithEmptyPage_when_gettingUsersAndNoRecordsMatch() {
        // when
        var response = userService.newUserAuthenticatedRequest()
                .param("username", "ziggy")
                .get(users());

        // then
        response.then()
                .statusCode(is(OK))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("number", equalTo(0))
                .body("numberOfElements", equalTo(0))
                .body("totalElements", equalTo(0))
                .body("totalPages", equalTo(0))
                .body("content", empty());
    }

    @Test
    public void shouldFilterAndReturnOkWithPage_when_gettingUsersWithPagingAndSortingCriteria() {
        // when
        var page0 = userService.newUserAuthenticatedRequest()
                .param("email", ".com") // this happens to eliminate the default user
                .param("page", "0")
                .param("size", "3")
                .param("sort", "email,desc")
                .get(users());

        // then
        page0.then()
                .statusCode(is(OK))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("number", equalTo(0))
                .body("numberOfElements", equalTo(3))
                .body("totalElements", equalTo(5))
                .body("totalPages", equalTo(2));

        assertThat(page0.jsonPath().getList("content"), contains(
                postUserResponseBodies.get("willy").getMap("$"),
                postUserResponseBodies.get("magnificentwilly").getMap("$"),
                postUserResponseBodies.get("georgie3").getMap("$")
        ));

        // when
        var page1 = userService.newUserAuthenticatedRequest()
                .param("email", ".com") // this happens to eliminate the default user
                .param("page", "1")
                .param("size", "3")
                .param("sort", "email,desc")
                .get(users());

        // then
        page1.then()
                .statusCode(is(OK))
                .contentType(APPLICATION_GDABSKI_DEMO_USER_V1)
                .body("number", equalTo(1))
                .body("numberOfElements", equalTo(2))
                .body("totalElements", equalTo(5))
                .body("totalPages", equalTo(2));

        assertThat(page1.jsonPath().getList("content"), contains(
                postUserResponseBodies.get("george2").getMap("$"),
                postUserResponseBodies.get("george1").getMap("$")
        ));
    }

    // more tests should follow

}
