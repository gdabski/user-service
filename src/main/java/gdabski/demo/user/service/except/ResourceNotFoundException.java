package gdabski.demo.user.service.except;

import lombok.Getter;

/**
 * Represents a failure to find a resource by ID.
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final int id;

    public ResourceNotFoundException(int id) {
        super(String.format("Resource with ID %s doesn't exist.", id));
        this.id = id;
    }
}
