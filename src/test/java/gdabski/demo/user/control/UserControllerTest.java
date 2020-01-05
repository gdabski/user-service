package gdabski.demo.user.control;

import static gdabski.demo.user.domain.UserState.INACTIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.CREATED;

import gdabski.demo.user.dto.UserDtoFixtures;
import gdabski.demo.user.dto.UserSummary;
import gdabski.demo.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class UserControllerTest {

    private final UserService userService = mock(UserService.class);
    private final UserController controller = new UserController(userService);

    @Test
    public void shouldCallServiceToCreateUser() {
        // given
        var specification = UserDtoFixtures.newUserSpecification();
        var summary = UserDtoFixtures.newUserSummary();

        when(userService.createUser(any())).thenReturn(summary);

        // when
        var returned = controller.createUser(specification);

        // then
        verify(userService).createUser(specification);
        verifyNoMoreInteractions(userService);

        assertEquals(CREATED, returned.getStatusCode());
        assertEquals(summary, returned.getBody());
    }

    @Test
    public void shouldCallServiceToFindUser() {
        // given
        int id = 4;
        var summary = UserDtoFixtures.newUserSummary();

        when(userService.findUser(anyInt())).thenReturn(summary);

        // when
        var returned = controller.findUser(id);

        // then
        verify(userService).findUser(id);
        verifyNoMoreInteractions(userService);

        assertEquals(summary, returned);
    }

    @Test
    public void shouldCallServiceToFindUsers() {
        // given
        var pageable = PageRequest.of(1, 20);
        var searchCriteria = UserDtoFixtures.userSearchCriteriaBuilder();
        var page = Page.<UserSummary>empty();

        when(userService.findUsers(any(), any())).thenReturn(page);

        // when
        var returned = controller.findUsers(pageable, searchCriteria);

        // then
        verify(userService).findUsers(pageable, searchCriteria.build());
        verifyNoMoreInteractions(userService);

        assertEquals(page, returned);
    }

    @Test
    public void shouldCallServiceToUpdateUser() {
        // given
        int id = 4;
        var patch = UserDtoFixtures.userPatchBuilder().state(INACTIVE).build();
        var summary = UserDtoFixtures.newUserSummary();

        when(userService.patchUser(anyInt(), any())).thenReturn(summary);

        // when
        var returned = controller.updateUser(id, patch);

        // then
        verify(userService).patchUser(id, patch);
        verifyNoMoreInteractions(userService);

        assertEquals(summary, returned);
    }

    @Test
    public void shouldCallServiceToDeleteUser() {
        // given
        int id = 4;

        // when
        controller.deleteUser(id);

        // then
        verify(userService).deleteUser(id);
        verifyNoMoreInteractions(userService);
    }


}