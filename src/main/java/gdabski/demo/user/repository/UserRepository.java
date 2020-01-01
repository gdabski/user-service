package gdabski.demo.user.repository;

import java.util.Optional;

import gdabski.demo.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds {@link User}s filtered by username, name and email substrings.
     * @param username part of the username that should match, {@code null} if entities
     *                 shouldn't be filtered by username
     * @param name part of the name that should match, {@code null} if entities
     *             shouldn't be filtered by name
     * @param email part of the email that should match, {@code null} if entities
     *              shouldn't be filtered by email
     * @param pageable {@link Pageable} describing the page number, page size and sorting
     * @return {@link Page} containing the selected {@link User}s.
     */
    @Query("SELECT u FROM User u WHERE (:username IS NULL OR u.username LIKE %:username%)" +
            "AND (:name IS NULL OR u.name LIKE %:name%)" +
            "AND (:email IS NULL OR u.email LIKE %:email%)")
    Page<User> findAllByUsernameNameAndEmail(String username, String name, String email, Pageable pageable);

    /**
     * Finds one (and presumably the only one) {@link User} with the given username.
     * @param username the user's username
     * @return {@link Optional} containing {@code User} he found user, empty {@code Optional}
     * if user not found
     */
    Optional<User> findOneByUsername(String username);
}
