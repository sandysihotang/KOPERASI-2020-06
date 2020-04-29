package io.github.sandy.repository;

import io.github.sandy.model.AnggotaKoperasi;
import io.github.sandy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface AngotaKoperasiRepository extends JpaRepository<AnggotaKoperasi, Integer> {
    Optional<AnggotaKoperasi> findFirstByUser(User user);

    Set<AnggotaKoperasi> getByUser(User user);
}
