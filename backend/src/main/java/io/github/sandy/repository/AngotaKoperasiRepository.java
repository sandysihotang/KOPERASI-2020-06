package io.github.sandy.repository;

import io.github.sandy.model.AnggotaKoperasi;
import io.github.sandy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface AngotaKoperasiRepository extends JpaRepository<AnggotaKoperasi, Integer> {
    Optional<AnggotaKoperasi> findFirstByUser(User user);

    Set<AnggotaKoperasi> getByUser(User user);

    AnggotaKoperasi getFirstByUser(User user);

    @Query(value = "SELECT count(u.id) from anggota_koperasi a " +
            "INNER JOIN users u " +
            "ON u.id = a.id_user " +
            "WHERE a.id_koperasi = ?1 AND u.enabled = ?2",
            nativeQuery = true)
    Integer getCountJumlahAnggota(Integer idKoperasi, Boolean status);
}
