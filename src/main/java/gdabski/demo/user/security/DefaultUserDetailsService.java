package gdabski.demo.user.security;

import static gdabski.demo.user.domain.UserState.INACTIVE;
import static java.util.stream.Collectors.toSet;
import static org.springframework.security.core.userdetails.User.builder;

import java.util.Collection;
import java.util.Set;

import gdabski.demo.user.domain.UserRole;
import gdabski.demo.user.entity.User;
import gdabski.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Looks up users for authentication purposes.
 */
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User entity = repository.findOneByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return toUserDetails(entity);
    }

    private static UserDetails toUserDetails(User entity) {
        return builder()
                .username(entity.getUsername())
                .password(entity.getPassword())
                .authorities(buildAuthorities(entity.getRoles()))
                .disabled(entity.getState() == INACTIVE)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();
    }

    private static Set<GrantedAuthority> buildAuthorities(Collection<UserRole> roles) {
        return roles.stream()
                .map(role -> String.format("ROLE_%s", role.getName()))
                .map(SimpleGrantedAuthority::new)
                .collect(toSet());
    }
}
