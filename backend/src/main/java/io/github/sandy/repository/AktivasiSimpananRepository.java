package io.github.sandy.repository;

import io.github.sandy.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AktivasiSimpananRepository extends JpaRepository<AktivasiSimpanan, Integer> {
    Boolean existsByKoperasiAndUserAndJenisSimpanan(Koperasi koperasi, User user, Integer jenisSimpanan);

    List<AktivasiSimpanan> findAllByKoperasi(Koperasi koperasi);

    AktivasiSimpanan getFirstByUserAndJenisSimpanan(User user, Integer jenisSimpanan);

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
