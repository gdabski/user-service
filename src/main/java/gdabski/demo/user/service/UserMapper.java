package gdabski.demo.user.service;

import static org.mapstruct.ReportingPolicy.IGNORE;

import gdabski.demo.user.dto.UserSpecification;
import gdabski.demo.user.dto.UserSummary;
import gdabski.demo.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface UserMapper {

    User toEntity(UserSpecification specification);
    UserSummary toSummary(User entity);

}
