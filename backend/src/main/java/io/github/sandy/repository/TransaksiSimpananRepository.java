package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.TransaksiSimpanan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TransaksiSimpananRepository extends JpaRepository<TransaksiSimpanan, Integer> {

    @Query(value = "SELECT t.jumlah_transaksi, t.id_aktivasi, t.created_at, t.kode_transaksi, t.jenis_transaksi," +
            " aktif, jenis_simpanan, total_simpanan, id_koperasi, id_user," +
            " first_name, last_name, address, no_telepon, user_id from transaksi_simpanan t " +
            "inner join aktivasi_simpanan a " +
            "on t.id_aktivasi = a.id " +
            "INNER JOIN users u on a.id_user = u.id " +
            "INNER JOIN user_detail ud on u.id = ud.user_id " +
            "where a.id_koperasi = ?1 " +
            "order by t.created_at DESC",
            nativeQuery = true)
    List<Map<String, Object>> findAllByKoperasi(Integer idKoperasi);

    @Query(value = "SELECT t.jumlah_transaksi, t.id_aktivasi, t.created_at, t.kode_transaksi, t.jenis_transaksi," +
            " aktif, jenis_simpanan, total_simpanan, id_koperasi, id_user," +
            " first_name, last_name, address, no_telepon, user_id from transaksi_simpanan t " +
            "inner join aktivasi_simpanan a " +
            "on t.id_aktivasi = a.id " +
            "INNER JOIN users u on a.id_user = u.id " +
            "INNER JOIN user_detail ud on u.id = ud.user_id " +
            "where a.id_user = ?1 " +
            "order by t.created_at DESC",
            nativeQuery = true)
    List<Map<String, Object>> findAllByUser(Integer user);

    @Query(value = "SELECT max(kode_transaksi) from transaksi_simpanan t " +
            "INNER JOIN aktivasi_simpanan s on t.id_aktivasi = s.id " +
            "WHERE s.id_koperasi = ?1",
            nativeQuery = true)
    String getMaxKodetransaksiSimpanan(Integer idKopersi);


    @Query(value = "SELECT t.id,t.jumlah_transaksi, t.id_aktivasi, t.created_at, t.kode_transaksi, t.jenis_transaksi," +
            " a.id_koperasi, a.id_user, a.aktif, a.jenis_simpanan, a.total_simpanan, a.tanggal_mulai FROM transaksi_simpanan t " +
            "INNER JOIN aktivasi_simpanan a ON t.id_aktivasi = a.id " +
            "WHERE a.id_user = ?1 " +
            "ORDER BY t.created_at DESC " +
            "LIMIT 5",
            nativeQuery = true)
    Set<Map<String, Object>> getTransaksiTerkini(Integer idUser);

    @Query(value = "SELECT " +
            "case " +
            "when ((SELECT count(t.jumlah_transaksi) FROM transaksi_simpanan t " +
            "INNER JOIN aktivasi_simpanan a ON t.id_aktivasi = a.id " +
            "WHERE a.id_koperasi = ?1 AND t.jenis_transaksi=?2 " +
            "AND a.jenis_simpanan=?3) > 0) " +
            "then (SELECT SUM(t.jumlah_transaksi) FROM transaksi_simpanan t INNER JOIN aktivasi_simpanan a ON t.id_aktivasi = a.id WHERE a.id_koperasi = ?1 AND t.jenis_transaksi=?2 AND a.jenis_simpanan=?3)" +
            "else 0 END",
            nativeQuery = true)
    Integer getJumlahYangMeminjam(Integer idKoperasi, Integer jenisTransaksi, Integer jenisSimpanan);

    @Query(value = "SELECT " +
            "case " +
            "when ((SELECT count(t.jumlah_transaksi) FROM transaksi_simpanan t " +
            "INNER JOIN aktivasi_simpanan a ON t.id_aktivasi = a.id " +
            "WHERE a.id_koperasi = ?1 AND t.jenis_transaksi=?2 " +
            "AND a.jenis_simpanan=?3 AND t.created_at >= ?4 AND t.created_at <= ?5 ) > 0) " +
            "then (SELECT SUM(t.jumlah_transaksi) FROM transaksi_simpanan t INNER JOIN aktivasi_simpanan a ON t.id_aktivasi = a.id WHERE a.id_koperasi = ?1 AND t.jenis_transaksi=?2 AND a.jenis_simpanan=?3 AND t.created_at >= ?4 AND t.created_at <= ?5)" +
            "else 0 END",
            nativeQuery = true)
    Long getTransaksi(Integer idKoperasi, Integer jenisTransaksi, Integer jenisSimpanan, Date from, Date to);

    @Query(value = "SELECT t.kode_transaksi,jumlah_transaksi, a.jenis_simpanan,t.jenis_transaksi,ud.first_name,ud.last_name, t.created_at FROM transaksi_simpanan t " +
            "INNER JOIN aktivasi_simpanan a ON t.id_aktivasi = a.id " +
            "inner join users u on a.id_user = u.id " +
            "inner join user_detail ud on u.id = ud.user_id " +
            "WHERE a.id_koperasi = ?1 " +
            "AND t.created_at >= ?2 AND t.created_at <= ?3",
            nativeQuery = true)
    List<Map<String, Object>> getTransaksiSimpananLaporan(Integer idKoperasi, Date from, Date to);
}
