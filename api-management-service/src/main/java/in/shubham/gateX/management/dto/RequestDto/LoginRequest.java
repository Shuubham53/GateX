package in.shubham.gateX.management.dto.RequestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "username must required")
    private String username;

    @NotBlank(message = "password must required")
    private String password;
}
