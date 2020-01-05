package gdabski.demo.user.security;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static gdabski.demo.user.domain.UserState.ACTIVE;

import java.util.Collection;
import java.util.Set;

import gdabski.demo.user.domain.UserRole;
import gdabski.demo.user.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@EqualsAndHashCode(of = "id")
public class AuthenticationUserDetails implements UserDetails {

    private final int id;
    private final String password;
    private final String username;
    private final Set<GrantedAuthority> authorities;
    private final boolean enabled;

    public AuthenticationUserDetails(User user) {
        this.id = user.getId();
        this.password = user.getPassword();
        this.username = user.getUsername();
        this.authorities = buildAuthorities(user.getRoles());
        this.enabled = user.getState() == ACTIVE;
    }

    private static Set<GrantedAuthority> buildAuthorities(Collection<UserRole> roles) {
        return roles.stream()
                .sorted()
                .map(role -> String.format("ROLE_%s", role.getName()))
                .map(SimpleGrantedAuthority::new)
                .collect(toImmutableSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
