package in.shubham.gateX.management.service;

import in.shubham.gateX.management.Exception.ApiException;
import in.shubham.gateX.management.Repository.ManagementUserRepository;
import in.shubham.gateX.management.Repository.OrganizationRepository;
import in.shubham.gateX.management.Repository.RoleRepository;
import in.shubham.gateX.management.dto.RequestDto.OrganizationRequest;
import in.shubham.gateX.management.dto.RequestDto.OrganizationStatusRequest;
import in.shubham.gateX.management.dto.RequestDto.TenantAdminRequest;
import in.shubham.gateX.management.dto.RequestDto.TenantOrganizationRequest;
import in.shubham.gateX.management.dto.ResponseDto.OrganizationResponse;
import in.shubham.gateX.management.dto.ResponseDto.TenantAdminResponse;
import in.shubham.gateX.management.entity.ManagementUser;
import in.shubham.gateX.management.entity.Organization;
import in.shubham.gateX.management.entity.OrganizationStatus;

import in.shubham.gateX.management.entity.Role;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final TenantContext tenantContext;
    private final ManagementUserRepository managementUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    public  OrganizationResponse createOrganization(OrganizationRequest request) {

        long startedAt = System.nanoTime();
        checkUnique(request.getName(),request.getSlug(),null);
        Organization organization = Organization.builder()
                .slug(request.getSlug())
                .name(request.getName())
                .createdAt(LocalDateTime.now())
                .status(OrganizationStatus.PENDING)
                .updatedAt(LocalDateTime.now())
                .build();
        organizationRepository.save(organization);
        return OrganizationResponse.from(organization);

    }

    @Transactional(readOnly = true)
    public List<OrganizationResponse> findAllOrganization() {

        List<OrganizationResponse> response = new ArrayList<>();
        for(Organization organization: organizationRepository.findByStatusNot(OrganizationStatus.DELETED)){
            response.add((OrganizationResponse.from(organization)));
        }
        return response;
    }

    @Transactional(readOnly = true)
    public  OrganizationResponse findOrganizationById(Long id) {
        Organization organization = getOrganization(id);
        return OrganizationResponse.from(organization);
    }

    @Transactional
    public OrganizationResponse updateOrganization(Long id, OrganizationRequest request) {
        Organization organization = getOrganization(id);

        checkUnique(request.getName(), request.getSlug(), id);

        organization.setName(request.getName());
        organization.setSlug(request.getSlug());
        organization.setUpdatedAt(LocalDateTime.now());

        return OrganizationResponse.from(organization);
    }

    @Transactional
    public OrganizationResponse changeStatusOfOrganization(Long id, OrganizationStatusRequest request) {
        Organization organization = getOrganization(id);
        if (request.getStatus() == OrganizationStatus.DELETED) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Status cannot be changed to DELETED. Use the delete endpoint instead.");
        }
        if (organization.getStatus() == request.getStatus()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Organization is already " + request.getStatus());
        }
        organization.setStatus(request.getStatus());
        organization.setUpdatedAt(LocalDateTime.now());
        return OrganizationResponse.from(organization);
    }

    @Transactional
    public void deleteOrganizationById(Long id) {
        Organization organization = getOrganization(id);
        organization.setStatus(OrganizationStatus.DELETED);
        organization.setDeletedAt(LocalDateTime.now());
        organization.setUpdatedAt(LocalDateTime.now());
    }

    private void checkUnique(String name, String slug, Long id) {
        boolean nameTaken = (id == null)
                ? organizationRepository.existsByNameIgnoreCase(name)
                : organizationRepository.existsByNameIgnoreCaseAndIdNot(name, id);
        if (nameTaken) {
            throw new ApiException(HttpStatus.CONFLICT, "Organization with this name already exists");
        }

        boolean slugTaken = (id == null)
                ? organizationRepository.existsBySlugIgnoreCase(slug)
                : organizationRepository.existsBySlugIgnoreCaseAndIdNot(slug, id);
        if (slugTaken) {
            throw new ApiException(HttpStatus.CONFLICT, "Organization with this slug already exists");
        }
    }

    private Organization getOrganization(Long id){
        return organizationRepository.findByIdAndStatusNot(id,OrganizationStatus.DELETED)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"Organization not found"));
    }

    @Transactional(readOnly = true)
    public  OrganizationResponse getOwnOrganization() {
        return OrganizationResponse.from(tenantContext.getOrganization());
    }

    @Transactional
    public  OrganizationResponse updateOwnOrganization(TenantOrganizationRequest request) {

        Organization organization = tenantContext.getOrganization();
        if(!organization.getName().equalsIgnoreCase(request.getName()) &&
                organizationRepository.existsByNameIgnoreCase(request.getName())){
            throw new ApiException(HttpStatus.CONFLICT,"Tenant name already exist");
        }
        organization.setName(request.getName());
        organization.setUpdatedAt(LocalDateTime.now());
        return OrganizationResponse.from(organization);
    }

    @Transactional
    public TenantAdminResponse createTenantAdmin(Long organizationId, TenantAdminRequest request) {

        Organization organization = getOrganization(organizationId);

        if (managementUserRepository.existsByOrganization_IdAndRole_Name(organizationId, "TENANT_ADMIN")) {
            throw new ApiException(HttpStatus.CONFLICT, "This organization already has a tenant administrator");
        }

        String username = request.getUsername().trim().toLowerCase();
        if (managementUserRepository.existsByUsername(username)) {
            throw new ApiException(HttpStatus.CONFLICT, "Username already exists");
        }
        Role role = roleRepository.findByName("TENANT_ADMIN").orElseThrow(() ->
                new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"TENANT_ADMIN role is missing"));

        ManagementUser managementUser = ManagementUser.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .organization(organization)
                .enabled(request.isEnabled())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role(role)
                .build();
        managementUserRepository.save(managementUser);

        return TenantAdminResponse.from(managementUser);
    }

    public TenantAdminResponse getTenantAdmin(Long organizationId) {
        return TenantAdminResponse.from(getTenantAdminEntity(organizationId));
    }

    private ManagementUser getTenantAdminEntity(Long organizationId){
        return managementUserRepository.findByOrganizationId(organizationId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"Tenant administrator not found"));
    }

}
