package gdabski.demo.user.it.fixture;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(NON_NULL)
public class ITUserSpecification {

    private final String username;
    private final String password;
    private final String name;
    private final Set<String> roles;
    private final String email;
    private final String state;
    private final String comment;

    public static ITUserSpecificationBuilder defaultBuilder() {
        return builder()
                .username("joe123")
                .password("pa$$w0rd")
                .name("Joe Shmoe")
                .roles(Set.of("USER"))
                .email("joe123@example.com")
                .state("ACTIVE")
                .comment("Great user!");
    }

}




