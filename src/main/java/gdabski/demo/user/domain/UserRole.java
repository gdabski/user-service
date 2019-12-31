package gdabski.demo.user.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

/**
 * Roles a user can have in the system.
 */
@RequiredArgsConstructor @Getter
public enum UserRole {

    /**
     * Administrator.
     * */
    ADMIN("ADMIN"),
    /**
     * End-user.
     * */
    USER("USER");

    @JsonValue
    private final String name;

}
