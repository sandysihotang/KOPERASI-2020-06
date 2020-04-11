package io.github.sandy.repository;

import io.github.sandy.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findFirstByName(String name);
}
