package io.github.sandy.repository;

import io.github.sandy.model.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface DetailUserRepository extends JpaRepository<UserDetail, Integer> {

    @Query(value = "select id, first_name, last_name, address, no_telepon, user_id from user_detail " +
            "WHERE user_id=?1 LIMIT 1",
            nativeQuery = true)
    Map<String, Object> findUserDetail(Integer user);
}
