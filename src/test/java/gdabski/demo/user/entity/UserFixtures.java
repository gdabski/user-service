package gdabski.demo.user.entity;

import static gdabski.demo.user.domain.UserRole.USER;
import static gdabski.demo.user.domain.UserState.ACTIVE;

import java.util.EnumSet;

import gdabski.demo.user.entity.User.UserBuilder;

public final class UserFixtures {

    private UserFixtures() {}

    public static UserBuilder userBuilder() {
        return User.builder()
                .id(1)
                .username("joe123")
                .password("$2y$10$uqIUUWx8tnofW7sNSbJ2neFQWSYzVYiPRbTlVrx7x2JtLCG26s0T2")
                .name("Joe Shmoe")
                .roles(EnumSet.of(USER))
                .email("joe123@example.com")
                .state(ACTIVE)
                .comment("Great user!");
    }

    public static User newUser() {
        return userBuilder().build();
    }

}