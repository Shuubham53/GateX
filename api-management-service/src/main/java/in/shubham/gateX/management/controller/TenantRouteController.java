package in.shubham.gateX.management.controller;

import in.shubham.gateX.management.dto.RequestDto.RouteRequest;
import in.shubham.gateX.management.dto.RequestDto.RouteStatusRequest;
import in.shubham.gateX.management.dto.ResponseDto.RouteResponse;
import in.shubham.gateX.management.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant")
@RequiredArgsConstructor
public class TenantRouteController {

    private final RouteService routeService;

    @PostMapping("/routes")
    public ResponseEntity<RouteResponse> createRoute(@Valid @RequestBody RouteRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(routeService.createRoute(request));
    }

    @GetMapping("/routes")
    public ResponseEntity<List<RouteResponse>> getAllRouteForTenant(){
        return ResponseEntity.ok(routeService.getAllRoute());
    }

    @GetMapping("/routes/{id}")
    public ResponseEntity<RouteResponse> getRouteById(@PathVariable Long id){
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    @PutMapping("/routes/{id}")
    public ResponseEntity<RouteResponse> updateRoute(@PathVariable Long id,
                                                      @Valid @RequestBody RouteRequest request){
        return ResponseEntity.ok(routeService.updateRoute(id,request));
    }
    @PatchMapping("/routes/{id}/status")
    public ResponseEntity<RouteResponse> changeStatus (@PathVariable Long id,
                                                     @Valid @RequestBody RouteStatusRequest request){
        return ResponseEntity.ok(routeService.changeStatus(id,request));
    }
    @DeleteMapping("/routes/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id){
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }








}
