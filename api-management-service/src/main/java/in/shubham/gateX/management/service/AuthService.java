package in.shubham.gateX.management.service;

import in.shubham.gateX.management.Exception.ApiException;
import in.shubham.gateX.management.Repository.ManagementUserRepository;
import in.shubham.gateX.management.dto.RequestDto.LoginRequest;
import in.shubham.gateX.management.dto.ResponseDto.LoginResponse;
import in.shubham.gateX.management.entity.ManagementUser;
import in.shubham.gateX.management.entity.OrganizationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ManagementUserRepository managementUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    @Value("${security.jwt.expiration-seconds}")
    private long expirationSeconds;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request){

        ManagementUser user = managementUserRepository.findByUsername(request.getUsername()).
                orElseThrow(this::invalidCredentials);
        if(!user.isEnabled() || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())){
            throw  invalidCredentials();
        }
        if(user.getOrganization() != null && user.getOrganization().getStatus() != OrganizationStatus.ACTIVE){
            throw  new ApiException(HttpStatus.FORBIDDEN,"Organization is not active");
        }
        Instant issuedAt = Instant.now();
        String role = user.getRole().getName();
        Long organizationId = user.getOrganization() == null ? null : user.getOrganization().getId();

        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .issuer("gateX-api-management")
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plusSeconds(expirationSeconds))
                .subject(user.getUsername())
                .claim("role", role);

        if(organizationId != null){
            claims.claim("organization_id",organizationId);
        }

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header,claims.build())).getTokenValue();
        return new LoginResponse(token,"Bearer",expirationSeconds,role,organizationId);

    }
    public RuntimeException invalidCredentials(){
        return new ApiException(HttpStatus.UNAUTHORIZED,"Invalid username or password");

    }
}






