package io.github.sandy.repository;

import io.github.sandy.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    @Query(
            value = "SELECT * FROM permission " +
                    "INNER JOIN permission_role pr on permission.id = pr.permission_id" +
                    " where pr.role_id = ?1",
            nativeQuery = true
    )
    List<Permission> getByRole(Integer role);
}
