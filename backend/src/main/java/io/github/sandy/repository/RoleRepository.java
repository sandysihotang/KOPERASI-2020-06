package io.github.sandy.repository;

import io.github.sandy.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;


public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findFirstByName(String name);

    @Query(value = "SELECT role.id, name  from role inner join role_user ru on role.id = ru.role_id " +
            "where ru.user_id = ?1 LIMIT 1",
            nativeQuery = true)
    Map<String, Object> find(Integer id);
}
