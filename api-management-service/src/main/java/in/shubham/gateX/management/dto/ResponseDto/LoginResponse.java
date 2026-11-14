package in.shubham.gateX.management.dto.ResponseDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String token;
    private String tokenType;
    private long expireInSeconds;
    private String role;
    private Long organizationId;


}
