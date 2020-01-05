package gdabski.demo.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import gdabski.demo.user.dto.UserDtoFixtures;
import gdabski.demo.user.entity.UserFixtures;
import gdabski.demo.user.repository.UserRepository;
import gdabski.demo.user.service.except.DuplicateUsernameException;
import gdabski.demo.user.service.except.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class UserServiceTest {

    private final UserRepository repository = mock(UserRepository.class);
    private final UserMapper mapper = mock(UserMapper.class);
    private final UserService service = new UserService(repository, mapper);

    @Test
    public void shouldSaveEntityAndReturnSummary_on_createUser() {
        // given
        var specification = UserDtoFixtures.newUserSpecification();
        var entityWithoutId = UserFixtures.userBuilder().id(null).build();
        var entityWithId = UserFixtures.userBuilder().id(33).build();
        var summary = UserDtoFixtures.newUserSummary();

        when(mapper.toEntity(any())).thenReturn(entityWithoutId);
        when(repository.saveAndFlush(any())).thenReturn(entityWithId);
        when(mapper.toSummary(any())).thenReturn(summary);

        // when
        var returned = service.createUser(specification);

        // then
        verify(mapper).toEntity(specification);
        verify(repository).saveAndFlush(entityWithoutId);
        verify(mapper).toSummary(entityWithId);
        verifyNoMoreInteractions(repository, mapper);

        assertEquals(summary, returned);
    }

    @Test
    public void shouldThrowException_on_createUser_when_usernameAlreadyUsed() {
        // given
        var specification = UserDtoFixtures.newUserSpecification();
        var entity = UserFixtures.userBuilder().id(null).build();

        when(mapper.toEntity(any())).thenReturn(entity);
        when(repository.saveAndFlush(any())).thenThrow(new DataIntegrityViolationException("Unique constaint violation."));

        // when and then
        assertThrows(DuplicateUsernameException.class, () -> {
            service.createUser(specification);
        });

        verify(mapper).toEntity(specification);
        verify(repository).saveAndFlush(entity);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    public void shouldQueryForUserAndReturnSummary_on_findUser_when_userExists() {
        // given
        int id = 1;
        var entity = UserFixtures.newUser();
        var summary = UserDtoFixtures.newUserSummary();

        when(repository.findById(anyInt())).thenReturn(Optional.of(entity));
        when(mapper.toSummary(any())).thenReturn(summary);

        // when
        var returned = service.findUser(id);

        // then
        verify(repository).findById(id);
        verify(mapper).toSummary(entity);
        verifyNoMoreInteractions(repository, mapper);

        assertEquals(summary, returned);
    }

    @Test
    public void shouldThrowException_on_findUser_when_userNotExists() {
        // given
        int id = 1;

        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        // when and then
        assertThrows(ResourceNotFoundException.class, () -> {
            service.findUser(id);
        });

        verify(repository).findById(id);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    public void shouldQueryForUsersUsingSearchCriteriaAndReturnSummaries_on_findUsers() {
        // given
        var pageable = PageRequest.of(1, 10);
        var searchCriteria = UserDtoFixtures.newUserSearchCriteria();
        var searchResults = new PageImpl<>(Arrays.asList(
                UserFixtures.userBuilder().id(1).build(),
                UserFixtures.userBuilder().id(2).build()
        ));
        var summary1 = UserDtoFixtures.userSummaryBuilder().id(1).build();
        var summary2 = UserDtoFixtures.userSummaryBuilder().id(2).build();


        when(repository.findAllByUsernameNameAndEmail(any(), any(), any(), any()))
                .thenReturn(searchResults);
        when(mapper.toSummary(any())).thenReturn(summary1, summary2);

        // when
        var returned = service.findUsers(pageable, searchCriteria);

        // then
        verify(repository).findAllByUsernameNameAndEmail(searchCriteria.getUsername(),
                searchCriteria.getName(), searchCriteria.getEmail(), pageable);
        for (var entity : searchResults.getContent()) {
            verify(mapper).toSummary(entity);
        }
        verifyNoMoreInteractions(repository, mapper);

        assertEquals(List.of(summary1, summary2), returned.getContent());
    }

    @Test
    public void shouldSavePatchedUserAndReturnSummary_on_patchUser_when_userExists() {
        // given
        int id = 1;
        var patch = UserDtoFixtures.userPatchBuilder().build();
        var foundEntity = UserFixtures.newUser();
        var savedEntity = UserFixtures.newUser();
        var summary = UserDtoFixtures.newUserSummary();

        when(repository.getOne(anyInt())).thenReturn(foundEntity);
        when(repository.save(any())).thenReturn(savedEntity);
        when(mapper.toSummary(any())).thenReturn(summary);

        // when
        var returned = service.patchUser(id, patch);

        // then
        var inOrder = inOrder(repository, mapper);
        inOrder.verify(repository).getOne(id);
        inOrder.verify(mapper).patchEntity(foundEntity, patch);
        inOrder.verify(repository).save(foundEntity);
        inOrder.verify(mapper).toSummary(savedEntity);
        inOrder.verifyNoMoreInteractions();

        assertEquals(summary, returned);
    }

    @Test
    public void shouldThrowException_on_patchUser_when_userNotExists() {
        // given
        int id = 1;
        var patch = UserDtoFixtures.userPatchBuilder().build();

        when(repository.getOne(anyInt())).thenThrow(EntityNotFoundException.class);

        // when and then
        assertThrows(ResourceNotFoundException.class, () -> {
            service.patchUser(id, patch);
        });

        verify(repository).getOne(id);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    public void shouldDeleteUser_on_deleteUser() {
        // given
        int id = 1;

        // when
        service.deleteUser(id);

        // then
        verify(repository).deleteById(id);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    public void shouldThrowException_on_deleteUser_when_userNotExists() {
        // given
        int id = 1;

        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(anyInt());

        // when and then
        assertThrows(ResourceNotFoundException.class, () -> {
            service.deleteUser(id);
        });

        verify(repository).deleteById(id);
        verifyNoMoreInteractions(repository, mapper);
    }

}