package io.github.sandy.repository;

import io.github.sandy.model.PengaturanPinjaman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PengaturanPinjamanRepository extends JpaRepository<PengaturanPinjaman, Integer> {
    Optional<PengaturanPinjaman> findFirstById(int id);
}
