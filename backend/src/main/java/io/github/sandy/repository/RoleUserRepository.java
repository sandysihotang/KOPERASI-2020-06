package io.github.sandy.repository;

import io.github.sandy.model.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleUserRepository extends JpaRepository<RoleUser, Integer> {
}
