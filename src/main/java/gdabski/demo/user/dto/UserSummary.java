package gdabski.demo.user.dto;

import java.util.Set;

import gdabski.demo.user.domain.UserRole;
import gdabski.demo.user.domain.UserState;
import lombok.Builder;
import lombok.Value;

@Value @Builder
public class UserSummary {

    private final int id;
    private final String username;
    private final String name;
    private final Set<UserRole> roles;
    private final String email;
    private final UserState state;
    private final String comment;

}
