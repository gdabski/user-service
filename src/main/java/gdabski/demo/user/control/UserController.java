package gdabski.demo.user.control;

import static gdabski.demo.user.control.VendorMediaType.APPLICATION_GDABSKI_DEMO_USER_V1;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import gdabski.demo.user.dto.UserPatch;
import gdabski.demo.user.dto.UserSearchCriteria.UserSearchCriteriaBuilder;
import gdabski.demo.user.dto.UserSpecification;
import gdabski.demo.user.dto.UserSummary;
import gdabski.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    @ResponseStatus(OK)
    // could use a custom return type instead of Page
    public Page<UserSummary> findUsers(Pageable pageable, UserSearchCriteriaBuilder criteria) {
        return service.findUsers(pageable, criteria.build());
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public UserSummary findUser(@PathVariable int id) {
        return service.findUser(id);
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

}
