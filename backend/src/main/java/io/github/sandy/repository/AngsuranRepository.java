package io.github.sandy.repository;

import io.github.sandy.model.Angsuran;
import io.github.sandy.model.Pinjaman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AngsuranRepository extends JpaRepository<Angsuran, Integer> {

    List<Angsuran> getAllByPinjaman(Pinjaman pinjaman);
}
