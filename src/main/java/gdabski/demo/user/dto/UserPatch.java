package gdabski.demo.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Set;

import gdabski.demo.user.domain.UserRole;
import gdabski.demo.user.domain.UserState;
import lombok.*;

/**
 * DTO to convey full semnatics of a <a href=https://tools.ietf.org/html/rfc7386>RFC 7386</a>-like
 * PATCH request for a user resource.
 * <p><p>
 * Package-private setters are meant to be only used by deserialization.
 */
@ToString
@EqualsAndHashCode
public class UserPatch {

    @Getter
    @Size(max = 64)
    private String name;
    boolean hasName;

    @Getter
    @Size(min = 1, message = "Must specify at least one role.")
    private Set<UserRole> roles;
    boolean hasRoles;

    @Getter
    @Email
    private String email;
    boolean hasEmail;

    @Getter
    private UserState state;
    boolean hasState;

    @Getter
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
