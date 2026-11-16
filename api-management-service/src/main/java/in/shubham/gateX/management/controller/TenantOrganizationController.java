package in.shubham.gateX.management.controller;

import in.shubham.gateX.management.dto.RequestDto.TenantOrganizationRequest;
import in.shubham.gateX.management.dto.ResponseDto.OrganizationResponse;
import in.shubham.gateX.management.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tenant/organization")
public class TenantOrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    private ResponseEntity<OrganizationResponse> getOwnOrganization(){
        return ResponseEntity.ok(organizationService.getOwnOrganization());
    }
    @PutMapping
    private ResponseEntity<OrganizationResponse> updateOwnOrganization(@Valid @RequestBody TenantOrganizationRequest request){
        return ResponseEntity.ok(organizationService.updateOwnOrganization(request));
    }
}
