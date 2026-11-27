package in.shubham.gateX.management.dto.ResponseDto;

import in.shubham.gateX.management.entity.Organization;

import in.shubham.gateX.management.entity.RouteConfigurationVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteVersionResponse {

    private Long id;
    private Organization organization;
    private String routeId;
    private Long versionNumber;
    private String changeType;
    private String configurationSnapshot;

    private LocalDateTime createdAt;


    public static RouteVersionResponse from(RouteConfigurationVersion version){
        return RouteVersionResponse.builder()
                .id(version.getId())
                .organization(version.getOrganization())
                .changeType(version.getChangeType())
                .routeId(version.getRouteId())
                .versionNumber(version.getVersionNumber())
                .configurationSnapshot(version.getConfigurationSnapshot())
                .createdAt(version.getCreatedAt())
                .build();
    }
}
