package gdabski.demo.user.entity.convert;

import static gdabski.demo.user.domain.UserRole.ADMIN;
import static gdabski.demo.user.domain.UserRole.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

import gdabski.demo.user.domain.UserRole;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UserRolesToBitMapConverterTest {

    private final UserRolesToBitMapConverter converter = new UserRolesToBitMapConverter();

    @ParameterizedTest
    @MethodSource("expectedMappings")
    public void shouldMapToDatabaseColumn(Set<UserRole> roles, int expected) {
        // when
        int result = converter.convertToDatabaseColumn(roles);

        // then
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("expectedMappings")
    public void shouldMapToEntityAttribute(Set<UserRole> expected, int dbRepresentation) {
        // when
        Set<UserRole> result = converter.convertToEntityAttribute(dbRepresentation);

        // then
        assertEquals(expected, result);
    }

    static Stream<Arguments> expectedMappings() {
        return Stream.of(
                arguments(EnumSet.of(ADMIN), 1),
                arguments(EnumSet.of(USER), 2),
                arguments(EnumSet.of(ADMIN, USER), 3)
        );
    }

}