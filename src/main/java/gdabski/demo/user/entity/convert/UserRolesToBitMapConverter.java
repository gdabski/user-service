package gdabski.demo.user.entity.convert;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static gdabski.demo.user.domain.UserRole.ADMIN;
import static gdabski.demo.user.domain.UserRole.USER;

import javax.persistence.AttributeConverter;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import gdabski.demo.user.domain.UserRole;

public class UserRolesToBitMapConverter implements AttributeConverter<Set<UserRole>, Integer> {

    private static final BiMap<UserRole, Integer> MAPPING;

    static {
        MAPPING = ImmutableBiMap.<UserRole, Integer>builder()
                .put(ADMIN, 1)
                .put(USER, 2)
                .build();
    }

    @Override
    public Integer convertToDatabaseColumn(Set<UserRole> roles) {
        return roles.stream()
                .mapToInt(MAPPING::get)
                .sum();
    }

    @Override
    public Set<UserRole> convertToEntityAttribute(Integer value) {
        return MAPPING.entrySet().stream()
                .filter(entry -> (entry.getValue() & value) != 0)
                .map(Entry::getKey)
                .collect(toImmutableSet());
    }
}