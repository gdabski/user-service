package gdabski.demo.user.dto;

import javax.validation.constraints.*;
import java.util.Set;

import gdabski.demo.user.domain.UserRole;
import gdabski.demo.user.domain.UserState;
import lombok.Builder;
import lombok.Value;

@Value @Builder
public class UserSpecification {

    @NotNull
    @Pattern(regexp = "[A-Za-z0-9]{1,16}", message = "Username must be alphanumeric and up to 16 characters.")
    private final String username;

    @NotNull
    @Size(min = 1, max = 1024)
    private final String password;

    @Size(min = 1, max = 64)
    private final String name;

    @NotEmpty(message = "Must specify at least one role.")
    private final Set<UserRole> roles;

    @Email
    private final String email;

    @NotNull
    private final UserState state;

    @Size(min = 1)
    private final String comment;

}
