package gdabski.demo.user.service;

import static gdabski.demo.user.domain.UserRole.ADMIN;
import static gdabski.demo.user.domain.UserRole.USER;
import static gdabski.demo.user.domain.UserState.INACTIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.EnumSet;

import gdabski.demo.user.dto.UserDtoFixtures;
import gdabski.demo.user.entity.UserFixtures;
import org.junit.jupiter.api.Test;

class UserMapperTest {

    private final PasswordMapper passwordMapper = mock(PasswordMapper.class);
    private final UserMapper userMapper = new UserMapperImpl(passwordMapper);

    @Test
    public void shouldMapUserSpecificationToEntity() {
        // given
        var specification = UserDtoFixtures.newUserSpecification();

        when(passwordMapper.encode(any())).thenReturn("encoded");

        // when
        var entity = userMapper.toEntity(specification);

        // then
        assertNull(entity.getId());
        assertEquals(specification.getUsername(), entity.getUsername());
        assertEquals("encoded", entity.getPassword());
        assertEquals(specification.getName(), entity.getName());
        assertEquals(specification.getRoles(), entity.getRoles());
        assertEquals(specification.getEmail(), entity.getEmail());
        assertEquals(specification.getState(), entity.getState());
        assertEquals(specification.getComment(), entity.getComment());
    }

    @Test
    public void shouldMapUserSpecificationToEntity_when_optionalSpecificationPropertiesMissing() {
        // given
        var specification = UserDtoFixtures.userSpecificationBuilder()
                .name(null)
                .email(null)
                .comment(null)
                .build();

        when(passwordMapper.encode(any())).thenReturn("encoded");

        // when
        var entity = userMapper.toEntity(specification);

        // then
        assertNull(entity.getName());
        assertNull(entity.getEmail());
        assertNull(entity.getComment());
    }

    @Test
    public void shouldMapUserEntityToSummary() {
        // given
        var entity = UserFixtures.newUser();

        // when
        var summary = userMapper.toSummary(entity);

        // then
        assertEquals(entity.getId(), summary.getId());
        assertEquals(entity.getUsername(), summary.getUsername());
        assertEquals(entity.getName(), summary.getName());
        assertEquals(entity.getRoles(), summary.getRoles());
        assertEquals(entity.getEmail(), summary.getEmail());
        assertEquals(entity.getState(), summary.getState());
        assertEquals(entity.getComment(), summary.getComment());
    }

    @Test
    public void shouldMapUserEntityToSummary_when_optionalEntityPropertiesMissing() {
        // given
        var entity = UserFixtures.userBuilder()
                .name(null)
                .email(null)
                .comment(null)
                .build();

        // when
        var summary = userMapper.toSummary(entity);

        // then
        assertNull(summary.getName());
        assertNull(summary.getEmail());
        assertNull(summary.getComment());
    }

    @Test
    public void shouldMapPatchOntoEntity() {
        // given
        var defaultEntity = UserFixtures.newUser();
        var patchedEntity = UserFixtures.newUser();
        var patch = UserDtoFixtures.userPatchBuilder()
                .name("Jane Jetson")
                .roles(EnumSet.of(ADMIN, USER))
                .email("jane001@gmail.com")
                .state(INACTIVE)
                .comment("This is Jane.")
                .build();

        // when
        userMapper.patchEntity(patchedEntity, patch);

        // then
        assertEquals(defaultEntity.getId(), patchedEntity.getId());
        assertEquals(defaultEntity.getUsername(), patchedEntity.getUsername());
        assertEquals(defaultEntity.getPassword(), patchedEntity.getPassword());
        assertEquals(patch.getName(), patchedEntity.getName());
        assertEquals(patch.getRoles(), patchedEntity.getRoles());
        assertEquals(patch.getEmail(), patchedEntity.getEmail());
        assertEquals(patch.getState(), patchedEntity.getState());
        assertEquals(patch.getComment(), patchedEntity.getComment());
    }

    @Test
    public void shouldMapPatchOntoEntity_when_patchSetWithExplicitNulls() {
        // given
        var defaultEntity = UserFixtures.newUser();
        var patchedEntity = UserFixtures.newUser();
        var patch = UserDtoFixtures.userPatchBuilder()
                .name(null)
                .email(null)
                .comment(null)
                .build();

        // when
        userMapper.patchEntity(patchedEntity, patch);

        // then
        assertEquals(defaultEntity.getId(), patchedEntity.getId());
        assertEquals(defaultEntity.getUsername(), patchedEntity.getUsername());
        assertEquals(defaultEntity.getPassword(), patchedEntity.getPassword());
        assertEquals(patch.getName(), patchedEntity.getName());
        assertEquals(defaultEntity.getRoles(), patchedEntity.getRoles());
        assertEquals(patch.getEmail(), patchedEntity.getEmail());
        assertEquals(defaultEntity.getState(), patchedEntity.getState());
        assertEquals(patch.getComment(), patchedEntity.getComment());
    }

    @Test
    public void shouldMapPatchOntoEntity_skippingPropertiesUnsetInPatch() {
        // given
        var defaultEntity = UserFixtures.newUser();
        var patchedEntity = UserFixtures.newUser();
        var patch = UserDtoFixtures.userPatchBuilder().build();

        // when
        userMapper.patchEntity(patchedEntity, patch);

        // then
        assertEquals(defaultEntity.getId(), patchedEntity.getId());
        assertEquals(defaultEntity.getUsername(), patchedEntity.getUsername());
        assertEquals(defaultEntity.getPassword(), patchedEntity.getPassword());
        assertEquals(defaultEntity.getName(), patchedEntity.getName());
        assertEquals(defaultEntity.getRoles(), patchedEntity.getRoles());
        assertEquals(defaultEntity.getEmail(), patchedEntity.getEmail());
        assertEquals(defaultEntity.getState(), patchedEntity.getState());
        assertEquals(defaultEntity.getComment(), patchedEntity.getComment());
    }

}