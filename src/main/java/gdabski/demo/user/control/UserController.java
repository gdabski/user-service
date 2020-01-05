package gdabski.demo.user.control;

import static gdabski.demo.user.control.MediaTypes.APPLICATION_GDABSKI_DEMO_USER_V1;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

import gdabski.demo.user.dto.ErrorResponse;
import gdabski.demo.user.dto.UserPatch;
import gdabski.demo.user.dto.UserSearchCriteria.UserSearchCriteriaBuilder;
import gdabski.demo.user.dto.UserSpecification;
import gdabski.demo.user.dto.UserSummary;
import gdabski.demo.user.service.UserService;
import gdabski.demo.user.service.except.DuplicateUsernameException;
import gdabski.demo.user.service.except.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(
        value = "users",
        produces = APPLICATION_GDABSKI_DEMO_USER_V1)
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserSummary> createUser(@RequestBody @Valid @NotNull UserSpecification specification) {
        log.info("Got request to create new user.");
        UserSummary user = service.createUser(specification);
        log.info("User created: {}.", user.getId());
        return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public UserSummary findUser(@PathVariable int id) {
        log.info("Got request to find user: {}.", id);
        UserSummary user = service.findUser(id);
        log.info("Returning user: {}.", id);
        return user;
    }

    @GetMapping
    @ResponseStatus(OK)
    // could use a custom return type instead of Page
    public Page<UserSummary> findUsers(Pageable pageable, UserSearchCriteriaBuilder criteria) {
        log.info("Got request to find users: criteria {}, pageable {}.", criteria, pageable);
        Page<UserSummary> users = service.findUsers(pageable, criteria.build());
        log.info("Returning paged users: {}.", users);
        return users;
    }

    @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    // user is able to make himself an admin here...
    public UserSummary updateUser(@PathVariable int id, @RequestBody @Valid @NotNull UserPatch patch) {
        log.info("Got request to update user: {}.", id);
        UserSummary user = service.patchUser(id, patch);
        log.info("Update user: {}.", id);
        return user;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        log.info("Got request to delete user: {}.", id);
        service.deleteUser(id);
        log.info("Deleted user: {}", id);
    }

    @PutMapping("/{id}/password")
    public void changePassword(@RequestBody @NotNull String password) {
        throw new UnsupportedOperationException("Not yet implemeted :(");
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        log.info("User doesn't exist: {}.", e.getId());
        return ErrorResponse.builder()
                .message(String.format("User doesn't exist: %d.", e.getId()))
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMessageConversionException(HttpMessageConversionException e, HttpServletRequest request) {
        log.info("Got malformed request at {} {}", request.getMethod(), request.getRequestURI());
        return ErrorResponse.builder()
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.info("Got semantically invalid request at {} {}", request.getMethod(), request.getRequestURI());
        return ErrorResponse.builder()
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleDuplicateUsername(DuplicateUsernameException e, HttpServletRequest request) {
        log.info("Attempt to create user with duplicate username: {}", e.getUsername());
        return ErrorResponse.builder()
                .message(String.format("Username already used: %s.", e.getUsername()))
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
