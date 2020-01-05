package gdabski.demo.user.dto;

import static gdabski.demo.user.domain.UserState.ACTIVE;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.EnumSet;
import java.util.Set;

import gdabski.demo.user.domain.UserRole;
import gdabski.demo.user.domain.UserState;
import lombok.*;

/**
 * DTO to convey full semantics of a <a href=https://tools.ietf.org/html/rfc7386>RFC 7386</a>-like
 * PATCH request for a user resource.
 * <p>
 * Package-private setters are meant to be only used by deserialization. Some fields are initialized
 * to specific default values to pass through validation, but as long as setters aren't called and
 * hasXyz flags aren't set these values shall be disregarded anyway.
 * <p>
 * The approach used here works but probably shouldn't be applied commercially.
 */
@ToString
@EqualsAndHashCode
public class UserPatch {

    UserPatch() {}

    @Getter
    @Size(min = 1, max = 64)
    private String name;
    boolean hasName;

    @Getter
    @NotEmpty(message = "Must specify at least one role.")
    private Set<UserRole> roles = EnumSet.allOf(UserRole.class);
    boolean hasRoles;

    @Getter
    @Email
    private String email;
    boolean hasEmail;

    @Getter
    @NotNull
    private UserState state = ACTIVE;
    boolean hasState;

    @Getter
    @Size(min = 1)
    private String comment;
    boolean hasComment;

    void setName(String name) {
        this.name = name;
        hasName = true;
    }

    void setRoles(Set<UserRole> roles) {
        this.roles = roles;
        hasRoles = true;
    }

    void setEmail(String email) {
        this.email = email;
        hasEmail = true;
    }

    void setState(UserState state) {
        this.state = state;
        hasState = true;
    }

    void setComment(String comment) {
        this.comment = comment;
        hasComment = true;
    }

    public boolean hasName() {
        return hasName;
    }

    public boolean hasRoles() {
        return hasRoles;
    }

    public boolean hasEmail() {
        return hasEmail;
    }

    public boolean hasState() {
        return hasState;
    }

    public boolean hasComment() {
        return hasComment;
    }

}
