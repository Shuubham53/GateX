package in.shubham.gateX.management.service;

import in.shubham.gateX.management.Exception.ApiException;
import in.shubham.gateX.management.Repository.OrganizationRepository;
import in.shubham.gateX.management.entity.Organization;
import in.shubham.gateX.management.entity.OrganizationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantContext {

    private final OrganizationRepository organizationRepository;

    public Organization getOrganization(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof JwtAuthenticationToken)){
            throw new ApiException(HttpStatus.FORBIDDEN,"Tenant identity is missing");
        }
        Long organizationId = ((JwtAuthenticationToken) authentication).getToken().getClaim("organization_id");

        if(organizationId == null){
            throw new ApiException(HttpStatus.FORBIDDEN,"Tenant identity is missing");
        }

        Organization organization = organizationRepository.findById(organizationId).
                orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN,"Organization does not exist"));

        if(organization.getStatus() != OrganizationStatus.ACTIVE){
            throw  new ApiException(HttpStatus.FORBIDDEN,"Organization is not active");
        }

        return organization;

    }
}
