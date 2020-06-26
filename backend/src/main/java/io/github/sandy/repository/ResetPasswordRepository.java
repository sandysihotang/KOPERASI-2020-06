package io.github.sandy.repository;

import io.github.sandy.model.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Integer> {

    @Query(value = "SELECT CASE WHEN (" +
            "SELECT COUNT(*) from reset_password where unique_id = ?1" +
            ") > 0 then true else false end",
            nativeQuery = true)
    Boolean existsByUniquId(String uuid);

    ResetPassword findFirstByUniqueId(String uuid);
}
