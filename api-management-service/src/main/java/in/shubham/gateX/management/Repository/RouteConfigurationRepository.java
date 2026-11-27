package in.shubham.gateX.management.Repository;

import in.shubham.gateX.management.entity.RouteConfigurationVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteConfigurationRepository extends JpaRepository<RouteConfigurationVersion,Long> {
    List<RouteConfigurationVersion> findAllByOrganizationIdAndRouteIdOrderByVersionNumberDesc(
            Long organizationId, String routeId);
}
