package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.KoperasiPengaturanPinjaman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KoperasiPengaturanPinjamanRepository extends JpaRepository<KoperasiPengaturanPinjaman, Integer> {
    Optional<KoperasiPengaturanPinjaman> getFirstByKoperasiAndStatus(Koperasi koperasi, Boolean status);

    Boolean existsByKoperasiAndStatus(Koperasi koperasi, Boolean status);
}

