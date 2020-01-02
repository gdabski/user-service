package gdabski.demo.user.control;

import static gdabski.demo.user.control.VendorMediaType.APPLICATION_GDABSKI_DEMO_USER_V1;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import gdabski.demo.user.dto.*;
import gdabski.demo.user.dto.UserSearchCriteria.UserSearchCriteriaBuilder;
import gdabski.demo.user.service.UserService;
import gdabski.demo.user.service.except.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(
        value = "user",
        consumes = {APPLICATION_JSON_VALUE, APPLICATION_GDABSKI_DEMO_USER_V1},
        produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    @ResponseStatus(CREATED)
    public UserSummary createUser(@RequestBody @Valid @NotNull UserSpecification specification) {
        return service.createUser(specification);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public UserSummary findUser(@PathVariable int id) {
        return service.findUser(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    // could use a custom return type instead of Page
    public Page<UserSummary> findUsers(Pageable pageable, UserSearchCriteriaBuilder criteria) {
        return service.findUsers(pageable, criteria.build());
    }

    @PatchMapping("/{id}")
    @ResponseStatus(OK)
    // user is able to make himself an admin...
    public UserSummary updateUser(@PathVariable int id, @RequestBody @Valid @NotNull UserPatch patch) {
        return service.patchUser(id, patch);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        service.deleteUser(id);
    }

    @PutMapping("/{id}/password")
    public void changePassword(@RequestBody @NotNull String password) {
        throw new UnsupportedOperationException();
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        return ErrorResponse.builder()
                .message(String.format("User doesn't exist: %s.", e.getId()))
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMessageConversionException(HttpMessageConversionException e, HttpServletRequest request) {
        return ErrorResponse.builder()
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleDefaultException(Exception e, HttpServletRequest request) {
        log.error("Failed to handle request. The exception details are:", e);
        return ErrorResponse.builder()
                .message("Unexpected error. Contact system administrator if problem persists.")
                .path(request.getRequestURI())
                .build();
    }

}
