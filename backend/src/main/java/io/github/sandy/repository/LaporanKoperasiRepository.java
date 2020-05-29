package io.github.sandy.repository;

import io.github.sandy.model.LaporanKoperasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface LaporanKoperasiRepository extends JpaRepository<LaporanKoperasi, Integer> {
    @Query(value = "SELECT case when (" +
            "SELECT count(*) from laporan_koperasi where id_koperasi = ?1 and tahun_laporan = ?2" +
            ") > 0 then true else false end",
            nativeQuery = true)
    Boolean existBy(Integer id, Integer tahun);

    @Query(value = "SELECT * FROM laporan_koperasi where id_koperasi = ?1 order by tahun_laporan desc ",
            nativeQuery = true)
    List<Map<String, Object>> getAllByKoperasi(Integer id);

    @Query(value = "SELECT * FROM laporan_koperasi where id_koperasi = ?1 and status = 3 order by tahun_laporan desc ",
            nativeQuery = true)
    List<Map<String, Object>> getAllByKoperasiAndStatus(Integer id);

    @Query(value = "SELECT l.id, l.tahun_laporan, l.created_at, l.original_name," +
            "k.nama_koperasi,k.alamat_koperasi, l.status FROM laporan_koperasi l " +
            "INNER JOIN koperasi k on l.id_koperasi = k.id order by tahun_laporan desc ",
            nativeQuery = true)
    List<Map<String, Object>> getAllLaporan();

    @Query(value = "SELECT CASE WHEN (" +
            "SELECT count(*) from laporan_koperasi where id_koperasi = ?1 AND status = 3" +
            ") > 0 then true else false end",
            nativeQuery = true)
    Boolean existsByIdKoperasi(Integer id);


}
