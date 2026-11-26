package in.shubham.gateX.management.Repository;

import in.shubham.gateX.management.entity.ApiRoute;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiRouteRepository extends JpaRepository<ApiRoute,Long> {
    boolean existsByOrganizationIdAndRouteId(Long id,String routeId);

    List<ApiRoute> findAllByOrganizationId(Long id);
    List<ApiRoute> findAllByOrganizationIdOrderByCreatedAtDesc(Long id);

}

