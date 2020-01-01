package gdabski.demo.user.service;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.ReportingPolicy.IGNORE;

import gdabski.demo.user.dto.UserPatch;
import gdabski.demo.user.dto.UserSpecification;
import gdabski.demo.user.dto.UserSummary;
import gdabski.demo.user.entity.User;
import gdabski.demo.user.service.PasswordMapper.Password;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = IGNORE,
        uses = PasswordMapper.class,
        injectionStrategy = CONSTRUCTOR)
public interface UserMapper {

    @Mapping(target = "password", qualifiedBy = Password.class)
    User toEntity(UserSpecification specification);

    UserSummary toSummary(User entity);

    void patchEntity(@MappingTarget User entity, UserPatch patch);

}
