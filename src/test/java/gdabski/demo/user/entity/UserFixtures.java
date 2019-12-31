package gdabski.demo.user.entity;

import static gdabski.demo.user.domain.UserRole.USER;
import static gdabski.demo.user.domain.UserState.ACTIVE;

import java.util.EnumSet;

import gdabski.demo.user.entity.User.UserBuilder;

public final class UserFixtures {

    private UserFixtures() {}

    public static UserBuilder userBuilder() {
        return User.builder()
                .username("joe123")
                .password("$2y$10$uqIUUWx8tnofW7sNSbJ2neFQWSYzVYiPRbTlVrx7x2JtLCG26s0T2")
                .roles(EnumSet.of(USER))
                .state(ACTIVE);
    }

    public static UserBuilder saturatedUserBuilder() {
        return userBuilder()
                .id(1)
                .name("Joe Shmoe")
                .email("joe123@example.com")
                .comment("Great user!");
    }

    public static User newUser() {
        return userBuilder().build();
    }

    public static User newSaturatedUser() {
        return saturatedUserBuilder().build();
    }

}