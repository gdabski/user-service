package gdabski.demo.user.service;

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

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;

    public UserSummary createUser(UserSpecification specification) {
        User saved = repository.save(userMapper.toEntity(specification));
        return userMapper.toSummary(saved);
    }

    public UserSummary findUser(int id) {
        User found = repository.findById(id).orElseThrow(() -> new ObjectRetrievalFailureException(User.class, id));
        return userMapper.toSummary(found);
    }

    public void deleteUser(int id) {
        repository.deleteById(id);
    }

    public Page<UserSummary> findUsers(Pageable pageable, UserSearchCriteria criteria) {
        Page<User> found = repository.findAllByUsernameNameAndEmail(criteria.getUsername(),
                criteria.getName(), criteria.getEmail(), pageable);
        return found.map(userMapper::toSummary);
    }
}
