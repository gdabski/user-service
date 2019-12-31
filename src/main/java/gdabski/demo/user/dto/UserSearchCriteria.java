package gdabski.demo.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserSearchCriteria {

    private final String username;
    private final String name;
    private final String email;

    public static final class UserSearchCriteriaBuilder {

        public void setUsername(String username) {
            username(username);
        }

        public void setName(String name) {
            name(name);
        }

        public void setEmail(String email) {
            email(email);
        }

    }

}
