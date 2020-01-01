package gdabski.demo.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class PasswordMapperTest {

    private final PasswordEncoder encoder = mock(PasswordEncoder.class);
    private final PasswordMapper mapper = new PasswordMapper(encoder);

    @Test
    void shouldDelegateEncodingToEncoder() {
        // given
        when(encoder.encode(any())).thenReturn("hashed");

        // when
        String mapped = mapper.encode("password");

        // then
        verify(encoder).encode("password");
        assertEquals("hashed", mapped);

    }
}