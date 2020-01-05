package gdabski.demo.user.service.except;

import lombok.Getter;

@Getter
public class DuplicateUsernameException extends RuntimeException {

    private final String username;

    public DuplicateUsernameException(String username) {
        super(String.format("Username %s is already used.", username));
        this.username = username;
    }

}
