package gdabski.demo.user.service;

import gdabski.demo.user.dto.UserPatch;
import gdabski.demo.user.dto.UserSearchCriteria;
import gdabski.demo.user.dto.UserSpecification;
import gdabski.demo.user.dto.UserSummary;
import gdabski.demo.user.entity.User;
import gdabski.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;

    public UserSummary createUser(UserSpecification specification) {
        User saved = repository.save(userMapper.toEntity(specification));
        return userMapper.toSummary(saved);
    }

    @Transactional(readOnly = true)
    public UserSummary findUser(int id) {
        User found = repository.findById(id).orElseThrow(() -> new ObjectRetrievalFailureException(User.class, id));
        return userMapper.toSummary(found);
    }

    @Transactional(readOnly = true)
    public Page<UserSummary> findUsers(Pageable pageable, UserSearchCriteria criteria) {
        Page<User> found = repository.findAllByUsernameNameAndEmail(criteria.getUsername(),
                criteria.getName(), criteria.getEmail(), pageable);
        return found.map(userMapper::toSummary);
    }

    public UserSummary patchUser(int id, UserPatch patch) {
        User entity = repository.getOne(id);
        userMapper.patchEntity(entity, patch);
        return userMapper.toSummary(repository.save(entity));
    }

    public void deleteUser(int id) {
        repository.deleteById(id);
    }

    // EntityNotFoundException
    // ObjectRetrievalFailureException
    // EmptyResultDataAccessException

}
