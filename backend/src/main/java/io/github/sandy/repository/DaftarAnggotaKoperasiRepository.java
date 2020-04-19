package io.github.sandy.repository;

import io.github.sandy.model.FieldDaftarAnggota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DaftarAnggotaKoperasiRepository extends JpaRepository<FieldDaftarAnggota, Integer> {
    Optional<FieldDaftarAnggota> findByKoperasiId(int id);
}
