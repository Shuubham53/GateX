package in.shubham.gateX.management.Repository;

import in.shubham.gateX.management.entity.ManagementUser;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagementUserRepository extends JpaRepository<ManagementUser,Long> {
    Optional<ManagementUser> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByOrganization_IdAndRole_Name(Long organizationId, String tenantAdmin);
    Optional<ManagementUser> findByOrganizationId(Long id);
}
