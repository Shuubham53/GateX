package in.shubham.gateX.management.Repository;

import in.shubham.gateX.management.entity.Organization;
import in.shubham.gateX.management.entity.OrganizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization,Long> {

    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    boolean existsBySlugIgnoreCase(String slug);
    boolean existsBySlugIgnoreCaseAndIdNot(String slug, Long id);

    List<Organization> findByStatusNot(OrganizationStatus status);
    Optional<Organization> findByIdAndStatusNot(Long id, OrganizationStatus status);
}
