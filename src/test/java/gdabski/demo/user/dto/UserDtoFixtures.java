package gdabski.demo.user.dto;

import static gdabski.demo.user.domain.UserRole.USER;
import static gdabski.demo.user.domain.UserState.ACTIVE;

import java.util.EnumSet;

import gdabski.demo.user.dto.UserSpecification.UserSpecificationBuilder;

public final class UserDtoFixtures {

    private UserDtoFixtures() {}

    public static UserSpecificationBuilder userSpecificationBuilder() {
        return UserSpecification.builder()
                .username("joe123")
                .password("pa$$w0rd")
                .name("Joe Shmoe")
                .roles(EnumSet.of(USER))
                .email("joe123@example.com")
                .state(ACTIVE)
                .comment("Great user!");
    }

    public static UserSpecification newUserSpecification() {
        return userSpecificationBuilder().build();
    }

}