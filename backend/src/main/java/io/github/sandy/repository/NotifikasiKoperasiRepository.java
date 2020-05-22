package io.github.sandy.repository;

import io.github.sandy.model.NotifikasiKoperasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface NotifikasiKoperasiRepository extends JpaRepository<NotifikasiKoperasi, Integer> {
    @Query(
            value = "SELECT CASE WHEN (" +
                    "SELECT count(*) from notifikasi_koperasi where id_koperasi = ?1 and status = ?2" +
                    ") > 0 then true else false end ",
            nativeQuery = true
    )
    Boolean getExistByKoperasi(Integer id, Boolean status);

    @Query(
            value = "SELECT count(*) from notifikasi_koperasi where id_koperasi = ?1 and status = false",
            nativeQuery = true
    )
    Integer getTotalNotifikasi(Integer id);

    @Modifying(clearAutomatically = true)
    @Query(value = "update notifikasi_koperasi set status= true where id_koperasi = ?1", nativeQuery = true)
    @Transactional
    void ubahStatus(Integer id);

    @Query(
            value = "SELECT CASE WHEN (" +
                    "SELECT count(*) from notifikasi_koperasi where id_koperasi = ?1" +
                    ") > 0 then true else false end ",
            nativeQuery = true
    )
    Boolean getExistByKoperasiKoperasi(Integer id);

    @Query(
            value = "SELECT lk.tahun_laporan, n.tanggal_pengiriman, n.pesan  from notifikasi_koperasi n " +
                    "INNER JOIN laporan_koperasi lk on n.id_laporan = lk.id where n.id_koperasi = ?1 order by tanggal_pengiriman desc",
            nativeQuery = true
    )
    List<Map<String, Object>> getAllByKoperasi(Integer id);
}
