package in.shubham.gateX.management.Repository;

import in.shubham.gateX.management.entity.ApiRoute;

import in.shubham.gateX.management.entity.OrganizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApiRouteRepository extends JpaRepository<ApiRoute,Long> {
    boolean existsByOrganizationIdAndRouteId(Long id,String routeId);

    List<ApiRoute> findAllByOrganizationId(Long id);
    List<ApiRoute> findAllByOrganizationIdOrderByCreatedAtDesc(Long id);
    Optional<ApiRoute> findByIdAndOrganizationId(Long id, Long organizationId);

    List<ApiRoute> findAllByActiveTrueAndOrganizationStatus(OrganizationStatus organizationStatus);
}

