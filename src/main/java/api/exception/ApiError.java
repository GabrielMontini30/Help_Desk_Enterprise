package api.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
