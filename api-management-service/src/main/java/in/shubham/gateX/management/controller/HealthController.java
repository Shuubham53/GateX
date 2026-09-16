package in.shubham.gateX.management.controller;


import in.shubham.gateX.management.dto.ResponseDto.HealthResponse;
import in.shubham.gateX.management.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health-check")
public class HealthController {

    private final HealthService healthService;

    @GetMapping
    public ResponseEntity<HealthResponse> healthCheck(){
        String dbStatus = healthService.getDbStatus();
        return  ResponseEntity.ok(new HealthResponse("UP",dbStatus, LocalDateTime.now()));
    }


}
