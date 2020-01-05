package gdabski.demo.user.it.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.springframework.http.HttpStatus;

public class HttpStatusMatcher extends BaseMatcher<Integer> {

    private final HttpStatus status;

    private HttpStatusMatcher(HttpStatus status) {
        this.status = status;
    }

    @Override
    public boolean matches(Object actual) {
        return actual instanceof Number && ((Number) actual).intValue() == status.value();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("status code " + status);
    }

    public static Matcher<Integer> is(HttpStatus status) {
        return new HttpStatusMatcher(status);
    }

}
