package io.github.sandy.repository;

import io.github.sandy.model.Pinjaman;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinjamanRepository extends JpaRepository<Pinjaman, Integer> {
}
