package in.shubham.gateX.management.controller;

import in.shubham.gateX.management.dto.RequestDto.OrganizationRequest;
import in.shubham.gateX.management.dto.RequestDto.OrganizationStatusRequest;
import in.shubham.gateX.management.dto.RequestDto.TenantAdminRequest;
import in.shubham.gateX.management.dto.ResponseDto.OrganizationResponse;
import in.shubham.gateX.management.dto.ResponseDto.TenantAdminResponse;
import in.shubham.gateX.management.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/platform/organizations")
public class PlatformOrganizationController {

    private final OrganizationService organizationService;

    // INFO : endpoints for organizations

    @PostMapping
    public ResponseEntity<OrganizationResponse> createOrganization(@Valid @RequestBody OrganizationRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationService.createOrganization(request)) ;
    }

    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> findAllOrganization(){
        return ResponseEntity.ok(organizationService.findAllOrganization());
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> findOrganizationById(@PathVariable Long id){
        return ResponseEntity.ok(organizationService.findOrganizationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponse> updateOrganization(@PathVariable Long id,
                                                                   @Valid @RequestBody OrganizationRequest request){
        return ResponseEntity.ok(organizationService.updateOrganization(id,request));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<OrganizationResponse> changeStatusOfOrganization(@PathVariable Long id,
                                                                   @Valid @RequestBody OrganizationStatusRequest request){
        return ResponseEntity.ok(organizationService.changeStatusOfOrganization(id,request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganizationById(@PathVariable Long id){
        organizationService.deleteOrganizationById(id);
        return ResponseEntity.noContent().build();
    }

    // INFO : endpoint for Tenant Admin

    @PostMapping("/{organizationId}/tenant-admin")
    public ResponseEntity<TenantAdminResponse> createTenantAdmin(@PathVariable Long organizationId,
                                                                 @Valid @RequestBody TenantAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(organizationService.createTenantAdmin(organizationId, request));
    }

    @GetMapping("/{organizationId}/tenant-admin")
    public ResponseEntity<TenantAdminResponse> getTenantAdmin(@PathVariable Long organizationId) {
        return ResponseEntity.ok(organizationService.getTenantAdmin(organizationId));
    }

    @PutMapping("/{organizationId}/tenant-admin")
    public ResponseEntity<TenantAdminResponse> updateTenantAdmin(@PathVariable Long organizationId,
                                                                 @Valid @RequestBody TenantAdminRequest request) {
        return ResponseEntity.ok(organizationService.updateTenantAdmin(organizationId, request));
    }

    @DeleteMapping("/{organizationId}/tenant-admin")
    public ResponseEntity<Void> deleteTenantAdmin(@PathVariable Long organizationId) {
        organizationService.deleteTenantAdmin(organizationId);
        return ResponseEntity.noContent().build();




}
