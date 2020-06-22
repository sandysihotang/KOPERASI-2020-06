package io.github.sandy.repository;

import io.github.sandy.model.*;
import org.omg.CORBA.INTERNAL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AktivasiSimpananRepository extends JpaRepository<AktivasiSimpanan, Integer> {
    @Query(value = "select case when (" +
            "SELECT count(*) from aktivasi_simpanan where id_koperasi=?1 AND id_user = ?2 AND jenis_simpanan = ?3" +
            ") > 0 then true else false end",
            nativeQuery = true)
    Boolean existsByKoperasiAndUserAndJenisSimpanan(Integer koperasi, Integer user, Integer jenisSimpanan);

    @Query(value = "SELECT a.created_at, a.tanggal_mulai, a.total_simpanan, a.jenis_simpanan," +
            "a.aktif from aktivasi_simpanan a " +
            "INNER JOIN users u on a.id_user = u.id " +
            "where a.id_koperasi = ?1",
            nativeQuery = true)
    List<Map<String, Object>> findAllByKoperasi(Integer koperasi);

    @Query(value = "SELECT total_simpanan " +
            "from aktivasi_simpanan  " +
            "where id_user = ?1 AND jenis_simpanan = ?2 limit 1",
            nativeQuery = true)
    Map<String, Object> getFirstByUserAndJenisSimpanan(Integer user, Integer jenisSimpanan);

    @Query(value = "SELECT sum(total_simpanan) from aktivasi_simpanan " +
            "WHERE id_user = ?1",
            nativeQuery = true)
    Long getSaldo(Integer idUser);

    @Query(value = "SELECT * from aktivasi_simpanan " +
            "WHERE id_user = ?1",
            nativeQuery = true)
    List<Map<String, Object>> getAllByUser(Integer user);

    @Query(value = "SELECT " +
            "case " +
            "when ((SELECT count(*) FROM aktivasi_simpanan " +
            "WHERE id_user = ?1 AND jenis_simpanan = ?2) > 0) " +
            "then (SELECT sum(total_simpanan) from aktivasi_simpanan " +
            "WHERE id_user = ?1 and jenis_simpanan = ?2)" +
            "else 0 END",
            nativeQuery = true)
    Long getTransaksi(Integer id, Integer jenis);

    @Query(value = "SELECT " +
            "case " +
            "when ((SELECT count(*) FROM aktivasi_simpanan " +
            "WHERE id_koperasi = ?1 AND jenis_simpanan = ?2) > 0) " +
            "then (SELECT sum(total_simpanan) from aktivasi_simpanan " +
            "WHERE id_koperasi = ?1 and jenis_simpanan = ?2)" +
            "else 0 END",
            nativeQuery = true)
    Long getTransaksiKoperasi(Integer id, Integer jenisSimpanan);
}
