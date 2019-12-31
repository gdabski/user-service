package gdabski.demo.user.entity;

import static lombok.AccessLevel.PRIVATE;

import javax.persistence.*;
import java.util.Set;

import gdabski.demo.user.domain.UserRole;
import gdabski.demo.user.domain.UserState;
import gdabski.demo.user.entity.convert.UserRolesToBitMapConverter;
import lombok.*;

@AllArgsConstructor(access = PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
@Builder
@Entity
public class User {

    protected User() {}

    @Id @GeneratedValue
    private Integer id;

    private String username;

    private String password;

    private String name;

    @Convert(converter = UserRolesToBitMapConverter.class)
    private Set<UserRole> roles;

    private String email;

    private UserState state;

    private String comment;

}
