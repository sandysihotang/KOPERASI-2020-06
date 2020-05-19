package io.github.sandy.repository;

import io.github.sandy.model.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface RoleUserRepository extends JpaRepository<RoleUser, Integer> {
    @Query(
            value = "SELECT * FROM role_user where user_id = ?1",
            nativeQuery = true
    )
    List<Map<String,Object>> findAllByIdUser(Integer id);

}
