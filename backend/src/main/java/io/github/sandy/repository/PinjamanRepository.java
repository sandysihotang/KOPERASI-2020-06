package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.Pinjaman;
import io.github.sandy.model.TransaksiSimpanan;
import io.github.sandy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface PinjamanRepository extends JpaRepository<Pinjaman, Integer> {
    Boolean existsByUserAndStatusIn(User user, Integer[] status);

    @Query(value = "select case when (" +
            "SELECT count(*) from peminjaman where id_user = ?1 AND status in ?2" +
            ") > 0 then true else false end ",
            nativeQuery = true)
    Boolean existsAdaPinjaman(Integer user, Integer[] status);

    @Query(value = "SELECT * from peminjaman WHERE id_user = ?1 AND status in ?2 limit 1",
            nativeQuery = true)
    Map<String, Object> getFirstByUserAndStatusIn(Integer user, Integer[] status);

    @Query(value = "SELECT * from peminjaman WHERE id_user = ?1 AND status = ?2",
            nativeQuery = true)
    List<Map<String, Object>> getByUserAndStatus(Integer user, Integer status);


    @Query(value = "SELECT max(kode_pinjaman) from peminjaman WHERE id_koperasi = ?1",
            nativeQuery = true)
    String getMaxKodePinjaman(Integer idKoperasi);

    @Query(value = "select case when " +
            "(SELECT count(*) from peminjaman WHERE id_koperasi = ?1 AND status = ?2) > 0 " +
            "then (SELECT sum(jumlah_pinjaman) from peminjaman WHERE id_koperasi = ?1 AND status = ?2) else 0 end",
            nativeQuery = true)
    Integer getPinjaman(Integer idKoperasi, Integer status);

    @Query(value = "select case when " +
            "(select count(*) from angsuran " +
            "inner join peminjaman p on angsuran.id_pinjaman = p.id where " +
            "p.id_koperasi = ?1 and angsuran.status_bayar = false and tanggal_jatuh_tempo < CURRENT_DATE) > 0" +
            " then (select sum(angsuran_pokok) from angsuran " +
            "inner join peminjaman p on angsuran.id_pinjaman = p.id where " +
            "p.id_koperasi = ?1 and angsuran.status_bayar = false and tanggal_jatuh_tempo < CURRENT_DATE)" +
            "else 0 end",
            nativeQuery = true)
    Integer getPinjamanJatuhTempo(Integer idKoperasi);

    @Query(value = "select case when (SELECT count(*) from peminjaman WHERE id_koperasi = ?1 AND status = ?2) > 0 then true else false end ",
            nativeQuery = true)
    Boolean existsByKoperasiAndStatus(Integer koperasi, Integer status);

    @Query(value = "SELECT " +
            "peminjaman.id, jumlah_pinjaman, jaminan, kode_pinjaman, " +
            " status, date_pengajuan_diterima, tenor, created_at, updated_at," +
            " bunga_pinjaman, min_tenor, max_tenor, ambang_batas_denda, persentase_denda, data" +
            " from peminjaman " +
            "INNER JOIN users u on peminjaman.id_user = u.id " +
            "INNER JOIN pengaturan_pinjaman pp on peminjaman.id_pengaturan_pinjaman = pp.id " +
            "INNER JOIN anggota_koperasi ak on u.id = ak.id_user" +
            " where peminjaman.id_koperasi = ?1", nativeQuery = true)
    List<Map<String, Object>> getAllByKoperasi(Integer koperasi);

    @Query(value = "SELECT " +
            "peminjaman.id, jumlah_pinjaman, jaminan, kode_pinjaman, peminjaman.tenor," +
            " status, date_pengajuan_diterima,  peminjaman.created_at, updated_at," +
            " bunga_pinjaman, min_tenor, max_tenor, ambang_batas_denda, persentase_denda, data" +
            " from peminjaman " +
            "INNER JOIN users u on peminjaman.id_user = u.id " +
            "INNER JOIN pengaturan_pinjaman pp on peminjaman.id_pengaturan_pinjaman = pp.id " +
            "INNER JOIN anggota_koperasi ak on u.id = ak.id_user" +
            " where peminjaman.id_koperasi = ?1 and status = ?2", nativeQuery = true)
    List<Map<String, Object>> getAllByKoperasiAndStatus(Integer koperasi, Integer status);

    @Query(value = "SELECT id, jumlah_pinjaman, jaminan, kode_pinjaman, id_user, id_koperasi, id_pengaturan_pinjaman, status, date_pengajuan_diterima, tenor, created_at, updated_at FROM peminjaman where id = ?1 LIMIT 1",
            nativeQuery = true)
    Map<String, Object> getFirstById(int id);

    @Query(value = "SELECT case when (" +
            "SELECT count(*) from peminjaman where id_user = ?1 AND status = ?2" +
            ")>0 then true else false end ",
            nativeQuery = true)
    Boolean existsByUserAndStatus(Integer user, int status);

    @Query(value = "SELECT * from peminjaman where id_user = ?1 AND status = ?2 limit 1",
            nativeQuery = true)
    Map<String, Object> getFirstByUserAndStatus(Integer user, int status);

    @Query(value = "SELECT p.id, kode_pinjaman, jaminan, jumlah_pinjaman, tenor," +
            "created_at, date_pengajuan_diterima, updated_at FROM peminjaman p " +
            "INNER JOIN users u on p.id_user = u.id " +
            "WHERE p.id_koperasi = ?1 " +
            "AND p.updated_at >= ?2 " +
            "AND p.updated_at <= ?3 " +
            "AND status = 6",
            nativeQuery = true)
    List<Map<String, Object>> getLaporanPinjaman(Integer idKoperasi, Date from, Date to);

    @Query(value = "select case when (" +
            "select count(*) from angsuran a " +
            "inner join peminjaman p on a.id_pinjaman = p.id " +
            "where p.id_koperasi = ?1 and a.status_bayar = false" +
            ") > 0 then (select sum(angsuran_pokok) from angsuran a " +
            "inner join peminjaman p on a.id_pinjaman = p.id " +
            "where p.id_koperasi = ?1 and a.status_bayar = false" +
            ") else 0 end", nativeQuery = true)
    Integer getpinjamanbelumbayar(Integer id);

    @Query(value = "select case when (" +
            "select count(*) from angsuran a " +
            "inner join peminjaman p on a.id_pinjaman = p.id " +
            "where p.id_koperasi = ?1 and a.status_bayar = false" +
            ") > 0 then (select sum(angsuran_pokok) from angsuran a " +
            "inner join peminjaman p on a.id_pinjaman = p.id " +
            "where p.id_koperasi = ?1 and a.status_bayar = true" +
            ") else 0 end", nativeQuery = true)
    Integer getpinjamanSudahBayarbelumbayar(Integer id);
}
