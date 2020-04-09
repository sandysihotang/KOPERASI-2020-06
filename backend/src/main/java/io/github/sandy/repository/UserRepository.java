package io.github.sandy.repository;

import io.github.sandy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User>findByUsername(String s);

    @Query(
            value = "SELECT * FROM user " +
                    "INNER JOIN role_user " +
                    "ON user.id = role_user.user_id " +
                    "WHERE user.enabled = 0 " +
                    "AND role_user.role_id = 2",
            nativeQuery = true
    )
    List<User> findByRole();

}
