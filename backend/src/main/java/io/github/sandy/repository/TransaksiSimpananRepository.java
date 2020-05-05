package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.TransaksiSimpanan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TransaksiSimpananRepository extends JpaRepository<TransaksiSimpanan, Integer> {

    @Query(value = "SELECT * from transaksi_simpanan t " +
            "inner join aktivasi_simpanan a " +
            "on t.id_aktivasi = a.id " +
            "where a.id_koperasi = ?1 " +
            "order by t.created_at DESC",
            nativeQuery = true)
    List<TransaksiSimpanan> findAllByKoperasi(Integer idKoperasi);

    @Query(value = "SELECT * from transaksi_simpanan t " +
            "inner join aktivasi_simpanan a " +
            "on t.id_aktivasi = a.id " +
            "where a.id_user = ?1 " +
            "order by t.created_at DESC",
            nativeQuery = true)
    List<TransaksiSimpanan> findAllByUser(Integer user);

    @Query(value = "SELECT max(kode_transaksi) from transaksi_simpanan t " +
            "INNER JOIN aktivasi_simpanan s on t.id_aktivasi = a.id " +
            "WHERE s.id_koperasi = ?1",
            nativeQuery = true)
    String getMaxKodetransaksiSimpanan(Integer idKopersi);


    @Query(value = "SELECT * FROM transaksi_simpanan t " +
            "INNER JOIN aktivasi_simpanan a ON t.id_aktivasi = a.id " +
            "WHERE a.id_user = ?1 " +
            "ORDER BY t.created_at DESC " +
            "LIMIT 5",
            nativeQuery = true)
    Set<TransaksiSimpanan> getTransaksiTerkini(Integer idUser);
}
