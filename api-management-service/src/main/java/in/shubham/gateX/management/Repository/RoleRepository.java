package in.shubham.gateX.management.Repository;

import in.shubham.gateX.management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {


    Optional<Role> findByName(String tenantAdmin);
}
