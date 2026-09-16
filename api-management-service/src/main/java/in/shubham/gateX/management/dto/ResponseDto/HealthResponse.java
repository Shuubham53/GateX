package in.shubham.gateX.management.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HealthResponse  {

    private String serviceStatus;
    private String dbStatus;
    private LocalDateTime timestamp;
}
