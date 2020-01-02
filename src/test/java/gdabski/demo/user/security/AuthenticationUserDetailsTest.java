package gdabski.demo.user.security;

import static gdabski.demo.user.domain.UserRole.ADMIN;
import static gdabski.demo.user.domain.UserRole.USER;
import static org.junit.jupiter.api.Assertions.*;

import java.util.EnumSet;
import java.util.List;

import gdabski.demo.user.entity.UserFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class AuthenticationUserDetailsTest {

    @Test
    public void shouldGetConstructedFromUserEntity() {
        // given
        var user = UserFixtures.userBuilder()
                .roles(EnumSet.of(USER, ADMIN))
                .build();

        // when
        var userDetails = new AuthenticationUserDetails(user);

        // then
        var expectedAuthorities = List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER"));

        assertEquals(user.getId(), userDetails.getId());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertIterableEquals(expectedAuthorities, userDetails.getAuthorities());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
    }

}