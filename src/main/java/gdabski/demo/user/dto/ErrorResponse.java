package gdabski.demo.user.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class ErrorResponse {
    private final String message;
    private final String path;
}
