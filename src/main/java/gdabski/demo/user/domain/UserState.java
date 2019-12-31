package gdabski.demo.user.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A user's activity state.
 */
@RequiredArgsConstructor @Getter
public enum UserState {

    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    @JsonValue
    private final String name;

}
