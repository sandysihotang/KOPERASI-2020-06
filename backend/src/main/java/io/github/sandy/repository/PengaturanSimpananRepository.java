package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.PengaturanSimpanan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PengaturanSimpananRepository extends JpaRepository<PengaturanSimpanan, Integer> {

    Boolean existsByKoperasi(Koperasi koperasi);

    List<PengaturanSimpanan> getAllByKoperasi(Koperasi koperasi);

    @Query(value = "SELECT * from aturan_simpanan " +
            "WHERE id_koperasi =?1 AND jenis_simpanan = ?2",
            nativeQuery = true)
    PengaturanSimpanan findAturan(Integer idKoperasi, Integer jenisSimpanan);

}
