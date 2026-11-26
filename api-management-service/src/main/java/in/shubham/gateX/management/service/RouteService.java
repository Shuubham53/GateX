package in.shubham.gateX.management.service;

import in.shubham.gateX.management.Exception.ApiException;
import in.shubham.gateX.management.Repository.ApiRouteRepository;
import in.shubham.gateX.management.dto.RequestDto.HeaderRuleRequest;
import in.shubham.gateX.management.dto.RequestDto.RouteRequest;
import in.shubham.gateX.management.dto.ResponseDto.RouteResponse;
import in.shubham.gateX.management.entity.ApiRoute;
import in.shubham.gateX.management.entity.Organization;
import in.shubham.gateX.management.entity.RouteHeaderRule;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final ApiRouteRepository apiRouteRepository;
    private final TenantContext tenantContext;

    @Transactional
    public  RouteResponse createRoute(RouteRequest request) {
        Organization organization = tenantContext.getOrganization();
        validate(organization,request);

        if(apiRouteRepository.existsByOrganizationIdAndRouteId(organization.getId(),request.getRouteId())){
            throw new ApiException(HttpStatus.CONFLICT,
                    "Route already exist for this organization");
        }
        ApiRoute route = new ApiRoute();
        route.setOrganization(organization);
        route.setConfigurationVersion(1L);
        route.setCreatedAt(LocalDateTime.now());
        apply(route,request);

        apiRouteRepository.save(route);
        return RouteResponse.from(route);
    }

    private void validate(Organization organization,RouteRequest request){
        String tenantPrefix = "/" + organization.getSlug() + "/";

        if(!request.getPathPattern().startsWith(tenantPrefix)){
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "path pattern must start with the tenant prefix : "+tenantPrefix);
        }
        if(!request.getTargetPathPrefix().startsWith("/")){
            throw new ApiException(HttpStatus.BAD_REQUEST,"target path prefix must start with / ");
        }

        try{
            URI uri = URI.create(request.getTargetBaseUrl());
            String scheme = uri.getScheme();
            boolean validScheme = "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
            if (!validScheme || uri.getHost() == null) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"target base url must be  valid HTTP or HTTPS URL");
        }

        normalizeMethods(request.getAllowedMethods());

        for(HeaderRuleRequest headerRuleRequest: request.getHeaderRules()){
            String direction = headerRuleRequest.getDirection().toUpperCase(Locale.ENGLISH);
            String action = headerRuleRequest.getAction().toUpperCase(Locale.ENGLISH);
            if(!"REQUEST".equals(direction) && !"RESPONSE".equals(direction)){
                throw new ApiException(HttpStatus.BAD_REQUEST,"Header direction must be REQUEST or RESPONSE");
            }
            if(!"ADD".equals(action) && !"REMOVE".equals(action)){
                throw new ApiException(HttpStatus.BAD_REQUEST,"Header action must be ADD or REMOVE");
            }
            if("ADD".equals(action) && (headerRuleRequest.getHeaderValue() == null
                    || headerRuleRequest.getHeaderValue().trim().isEmpty())){
                    throw new ApiException(HttpStatus.BAD_REQUEST,"ADD Header rules requires header value");
            }


        }
    }

    private Set<String> normalizeMethods(Set<String> methods){
        Set<String> normalized = new HashSet<>();
        for(String method : methods){
            String value = method.toUpperCase(Locale.ENGLISH);
            try{
                HttpMethod.valueOf(value);
            }catch (IllegalArgumentException exception){
                throw new ApiException(HttpStatus.BAD_REQUEST,"Unsupported HTTP method : "+method);
            }
            normalized.add(value);
        }
        return normalized;
    }

    private void apply(ApiRoute route,RouteRequest request){
        route.setRouteId(request.getRouteId());
        route.setName(request.getName());
        route.setPathPattern(request.getPathPattern());
        route.setTargetBaseUrl(request.getTargetBaseUrl());
        route.setTargetPathPrefix(request.getTargetPathPrefix());
        route.setRequestPerMinute(request.getRequestsPerMinute());
        route.setRequestTimeoutMs(request.getResponseTimeoutMs());
        route.setIdempotencyEnabled(request.isIdempotencyEnabled());
        route.setActive(request.isActive());
        route.setAllowedMethods(normalizeMethods(request.getAllowedMethods()));
        route.setUpdatedAt(LocalDateTime.now());

        route.getHeaderRules().clear();
        for(HeaderRuleRequest ruleRequest : request.getHeaderRules()) {
            RouteHeaderRule rule = new RouteHeaderRule();
            rule.setRoute(route);
            rule.setDirection(ruleRequest.getDirection().toUpperCase(Locale.ENGLISH));
            rule.setAction(ruleRequest.getAction().toUpperCase(Locale.ENGLISH));
            rule.setHeaderName(ruleRequest.getHeaderName());
            rule.setHeaderValue(ruleRequest.getHeaderValue());
            rule.setCreatedAt(LocalDateTime.now());
            rule.setUpdatedAt(LocalDateTime.now());
            route.getHeaderRules().add(rule);
        }
    }


    @Transactional(readOnly = true)
    public  List<RouteResponse> getAllRoute() {
        Organization organization = tenantContext.getOrganization();
        return toResponse(apiRouteRepository.findAllByOrganizationId(organization.getId()));
    }

    private List<RouteResponse> toResponse(List<ApiRoute> routes) {
        List<RouteResponse> response = new ArrayList<>();
        for(ApiRoute route: routes){
            response.add(RouteResponse.from(route));
        }
        return response;
    }
}
