package io.github.sandy.repository;

import io.github.sandy.model.NotifikasiAnggota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface NotifikasiAnggotaRepository extends JpaRepository<NotifikasiAnggota, Integer> {
    @Query(value = "select case when (" +
            "select count(*) from notifikasi_anggota where id_angsuran = ?1" +
            ") > 0 then true else false end",
            nativeQuery = true)
    Boolean checkExistBy(Integer id);

    @Query(value = "select case when (" +
            "select count(*) from notifikasi_anggota where id_user = ?1 and status = false" +
            ") > 0 then true else false end",
            nativeQuery = true)
    Boolean checkExistByUser(Integer id);

    @Query(value = "select case when (" +
            "select count(*) from notifikasi_anggota where id_user = ?1" +
            ") > 0 then true else false end",
            nativeQuery = true)
    Boolean checkExistByUserHome(Integer id);

    @Query(value = "select count(*) from notifikasi_anggota where id_user = ?1 and status = false",
            nativeQuery = true)
    Integer getTotal(Integer id);

    @Modifying(clearAutomatically = true)
    @Query(value = "update notifikasi_anggota set status= true where id_user = ?1", nativeQuery = true)
    @Transactional
    void ubahStatus(Integer id);

    @Query(value = "select * from notifikasi_anggota where id_user = ?1 order by tanggal_notifikasi desc ",
            nativeQuery = true)
    List<Map<String, Object>> getNotifikasiByUser(Integer id);
}
