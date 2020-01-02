package gdabski.demo.user.dto;

import static gdabski.demo.user.domain.UserRole.USER;
import static gdabski.demo.user.domain.UserState.ACTIVE;

import java.util.EnumSet;
import java.util.Set;

import gdabski.demo.user.domain.UserRole;
import gdabski.demo.user.domain.UserState;
import gdabski.demo.user.dto.UserSearchCriteria.UserSearchCriteriaBuilder;
import gdabski.demo.user.dto.UserSpecification.UserSpecificationBuilder;
import gdabski.demo.user.dto.UserSummary.UserSummaryBuilder;

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
        return userSpecificationBuilder()
                .build();
    }

    public static UserSummaryBuilder userSummaryBuilder() {
        return UserSummary.builder()
                .id(10)
                .username("joe123")
                .name("Joe Shmoe")
                .roles(EnumSet.of(USER))
                .email("joe123@example.com")
                .state(ACTIVE)
                .comment("Great user!");
    }

    public static UserSummary newUserSummary() {
        return userSummaryBuilder()
                .build();
    }

    public static UserSearchCriteriaBuilder userSearchCriteriaBuilder() {
        return UserSearchCriteria.builder()
                .username("joe123")
                .name("Joe Shmoe")
                .email("joe123@example.com");
    }

    public static UserSearchCriteria newUserSearchCriteria() {
        return userSearchCriteriaBuilder()
                .build();
    }

    public static class UserPatchBuilder {

        private final UserPatch patch = new UserPatch();

        private UserPatchBuilder() {}

        public UserPatchBuilder name(String name) {
            patch.setName(name);
            return this;
        }

        public UserPatchBuilder roles(Set<UserRole> roles) {
            patch.setRoles(roles);
            return this;
        }
        public UserPatchBuilder email(String email) {
            patch.setEmail(email);
            return this;
        }

        public UserPatchBuilder state(UserState state) {
            patch.setState(state);
            return this;
        }

        public UserPatchBuilder comment(String comment) {
            patch.setComment(comment);
            return this;
        }

        public UserPatch build() {
            return patch;
        }
    }

    public static UserPatchBuilder userPatchBuilder() {
        return new UserPatchBuilder();
    }

}