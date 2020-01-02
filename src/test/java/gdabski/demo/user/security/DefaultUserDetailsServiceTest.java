package gdabski.demo.user.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import gdabski.demo.user.entity.UserFixtures;
import gdabski.demo.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class DefaultUserDetailsServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final DefaultUserDetailsService service = new DefaultUserDetailsService(userRepository);

    @Test
    public void shouldReturnUserDetails_when_userFoundByUsername() {
        // given
        var entity = UserFixtures.newUser();
        var username = entity.getUsername();

        when(userRepository.findOneByUsername(any())).thenReturn(Optional.of(entity));

        // when
        var userDetails = service.loadUserByUsername(username);

        // then
        verify(userRepository).findOneByUsername(username);
        verifyNoMoreInteractions(userRepository);

        assertNotNull(userDetails);
    }

    @Test
    public void shouldThrowException_when_userNotFoundByUsername() {
        // given
        var username = "username";

        when(userRepository.findOneByUsername(any())).thenReturn(Optional.empty());

        // when and then
        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(username);
        });

        verify(userRepository).findOneByUsername(username);
        verifyNoMoreInteractions(userRepository);
    }

}