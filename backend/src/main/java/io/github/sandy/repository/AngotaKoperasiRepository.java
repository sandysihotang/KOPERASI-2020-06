package io.github.sandy.repository;

import io.github.sandy.model.AnggotaKoperasi;
import io.github.sandy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AngotaKoperasiRepository extends JpaRepository<AnggotaKoperasi, Integer> {
    Optional<AnggotaKoperasi> findFirstByUser(User user);
}
