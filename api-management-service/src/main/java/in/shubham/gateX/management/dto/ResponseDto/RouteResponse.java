package in.shubham.gateX.management.dto.ResponseDto;

import in.shubham.gateX.management.entity.ApiRoute;
import in.shubham.gateX.management.entity.RouteHeaderRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponse {

    private Long id;
    private Long organizationId;
    private String routeId;
    private String name;
    private String pathPattern;
    private String targetBaseUrl;
    private String targetPathPrefix;
    private Integer requestsPerMinute;
    private Integer responseTimeoutMs;
    private boolean idempotencyEnabled;
    private boolean active;
    private Long configurationVersion;
    private Set<String> allowedMethods;
    private List<HeaderRuleResponse> headerRules;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RouteResponse from(ApiRoute route) {
        List<HeaderRuleResponse> rules = new ArrayList<HeaderRuleResponse>();
        for(RouteHeaderRule rule : route.getHeaderRules()) {
            rules.add(HeaderRuleResponse.from(rule));
        }

        return RouteResponse.builder()
                .id(route.getId())
                .organizationId(route.getOrganization().getId())
                .routeId(route.getRouteId())
                .name(route.getName())
                .pathPattern(route.getPathPattern())
                .targetBaseUrl(route.getTargetBaseUrl())
                .targetPathPrefix(route.getTargetPathPrefix())
                .responseTimeoutMs(route.getResponseTimeoutMs())
                .requestsPerMinute(route.getRequestsPerMinute())
                .idempotencyEnabled(route.isIdempotencyEnabled())
                .configurationVersion(route.getConfigurationVersion())
                .headerRules(rules)
                .allowedMethods(route.getAllowedMethods())
                .createdAt(route.getCreatedAt())
                .updatedAt(route.getUpdatedAt())
                .build();
    }
}