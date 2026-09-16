package in.shubham.gateX.management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthService {
    
    private final JdbcTemplate jdbcTemplate;
    
    public String getDbStatus(){
        String databaseStatus = "DOWN";
        try{
            Integer result = jdbcTemplate.queryForObject("SELECT 1",Integer.class);
            if(Integer.valueOf(1).equals(result)){
                databaseStatus = "UP";
            }
        }catch (Exception e){
            log.error("PostgresSql Health check failed: {}",e.getMessage());
        }
        return databaseStatus;
    }
    

    
}
