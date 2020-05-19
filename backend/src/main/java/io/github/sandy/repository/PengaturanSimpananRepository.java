package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.PengaturanSimpanan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface PengaturanSimpananRepository extends JpaRepository<PengaturanSimpanan, Integer> {

    @Query(value = "select case when (" +
            "SELECT count(*) from aturan_simpanan " +
            "WHERE id_koperasi =?1)>0 then true else false end",
            nativeQuery = true)
    Boolean existsByKoperasi(Integer koperasi);

    @Query(value = "SELECT * from aturan_simpanan " +
            "WHERE id_koperasi =?1",
            nativeQuery = true)
    List<Map<String, Object>> getAllByKoperasi(Integer koperasi);

    @Query(value = "SELECT * from aturan_simpanan " +
            "WHERE id_koperasi =?1 AND jenis_simpanan = ?2",
            nativeQuery = true)
    Map<String, Object> findAturan(Integer idKoperasi, Integer jenisSimpanan);

}
