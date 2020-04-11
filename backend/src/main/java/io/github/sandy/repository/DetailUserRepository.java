package io.github.sandy.repository;

import io.github.sandy.model.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailUserRepository extends JpaRepository<UserDetail, Integer> {
}
