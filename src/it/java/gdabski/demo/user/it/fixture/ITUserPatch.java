package gdabski.demo.user.it.fixture;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(NON_NULL)
public class ITUserPatch {

    private final String name;
    private final Set<String> roles;
    private final String email;
    private final String state;
    private final String comment;

    public static ITUserPatch.ITUserPatchBuilder defaultBuilder() {
        return builder()
                .name("Fred Flintstone")
                .roles(Set.of("USER", "ADMIN"))
                .email("fred@bedrock.com")
                .state("ACTIVE")
                .comment("Fine bloke!");
    }

}




