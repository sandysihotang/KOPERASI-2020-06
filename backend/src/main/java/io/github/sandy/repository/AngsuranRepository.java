package io.github.sandy.repository;

import io.github.sandy.model.Angsuran;
import io.github.sandy.model.Pinjaman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AngsuranRepository extends JpaRepository<Angsuran, Integer> {

    @Query(value = "SELECT id, id_pinjaman, angsuran_pokok, bunga, status_bayar, tanggal_bayar, tanggal_jatuh_tempo, denda, total_bayar, total_angsuran, total_tagihan, urutan_ke FROM angsuran WHERE id_pinjaman=?1 ORDER BY urutan_ke ASC", nativeQuery = true)
    List<Map<String, Object>> getAllByPinjamanOrderByUrutanKeAsc(Integer pinjaman);

    @Query(value = "SELECT id, id_pinjaman, angsuran_pokok, bunga, status_bayar, tanggal_bayar, tanggal_jatuh_tempo, denda, total_bayar, total_angsuran, total_tagihan, urutan_ke FROM angsuran where id_pinjaman = ?1 AND status_bayar=?2 order by urutan_ke asc ", nativeQuery = true)
    List<Map<String, Object>> getAllByPinjamanAndStatusBayar(Integer pinjaman, Boolean status);

    @Query(value = "SELECT id, id_pinjaman, angsuran_pokok, bunga, status_bayar, tanggal_bayar, tanggal_jatuh_tempo, denda, total_bayar, total_angsuran, total_tagihan, urutan_ke FROM angsuran where id_pinjaman = ?1 AND status_bayar=?2 order by urutan_ke asc LIMIT 1", nativeQuery = true)
    Map<String, Object> getFirstByPinjamanAndStatusBayarOrderByUrutanKe(Integer pinjaman, Boolean status);

    Boolean existsByPinjamanAndStatusBayar(Pinjaman pinjaman, Boolean statusBayar);

    @Query(value = "SELECT * FROM angsuran " +
            "INNER JOIN peminjaman on angsuran.id_pinjaman = peminjaman.id " +
            "INNER JOIN pengaturan_pinjaman on peminjaman.id_pengaturan_pinjaman = pengaturan_pinjaman.id " +
            "WHERE angsuran.status_bayar = false AND angsuran.tanggal_jatuh_tempo < CURRENT_DATE", nativeQuery = true)
    List<Angsuran> getExistDenda();

    @Query(value = "SELECT " +
            "case " +
            "when ((SELECT count(*) FROM angsuran INNER JOIN peminjaman p on angsuran.id_pinjaman = p.id" +
            " where " +
            "EXTRACT(MONTH FROM tanggal_bayar) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND status_bayar = true AND p.id_koperasi = ?1) > 0) " +
            "then (SELECT SUM(bunga + denda) FROM angsuran INNER JOIN peminjaman p on angsuran.id_pinjaman = p.id " +
            "where EXTRACT(MONTH FROM tanggal_bayar) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND status_bayar = true AND p.id_koperasi = ?1)" +
            "else 0 END", nativeQuery = true)
    Long getRealisasiJasa(Integer idKoperasi);

    @Query(value = "SELECT " +
            "case " +
            "when ((SELECT count(*) FROM angsuran INNER JOIN peminjaman p on angsuran.id_pinjaman = p.id" +
            " where " +
            "EXTRACT(MONTH FROM tanggal_bayar) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND status_bayar = false AND p.id_koperasi = ?1) > 0) " +
            "then (SELECT SUM(bunga + denda) FROM angsuran INNER JOIN peminjaman p on angsuran.id_pinjaman = p.id " +
            "where EXTRACT(MONTH FROM tanggal_bayar) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND status_bayar = false AND p.id_koperasi = ?1)" +
            "else 0 END", nativeQuery = true)
    Long getPengeluaran(Integer idKoperasi);

    @Query(value = "SELECT " +
            "case " +
            "when ((SELECT count(*) FROM angsuran INNER JOIN peminjaman p on angsuran.id_pinjaman = p.id" +
            " where " +
            "tanggal_bayar >= ?2 AND tanggal_bayar<= ?3 " +
            "AND status_bayar = true AND p.id_koperasi = ?1) > 0) " +
            "then (SELECT SUM(bunga) FROM angsuran INNER JOIN peminjaman p on angsuran.id_pinjaman = p.id " +
            "where EXTRACT(MONTH FROM tanggal_bayar) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND status_bayar = true AND p.id_koperasi = ?1)" +
            "else 0 END", nativeQuery = true)
    Long getRealisasiJasaDateFromTo(Integer idKoperasi, Date from, Date to);

    @Query(value = "SELECT " +
            "case " +
            "when ((SELECT count(*) FROM angsuran INNER JOIN peminjaman p on angsuran.id_pinjaman = p.id" +
            " where " +
            "tanggal_bayar >= ?2 AND tanggal_bayar<= ?3 " +
            "AND status_bayar = true AND p.id_koperasi = ?1) > 0) " +
            "then (SELECT SUM(denda) FROM angsuran INNER JOIN peminjaman p on angsuran.id_pinjaman = p.id " +
            "where EXTRACT(MONTH FROM tanggal_bayar) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND status_bayar = true AND p.id_koperasi = ?1)" +
            "else 0 END", nativeQuery = true)
    Long getDenda(Integer idKoperasi, Date from, Date to);

    @Query(value = "SELECT bunga,denda from angsuran", nativeQuery = true)
    List<Map<String, Object>> getAllByPinjaman(Object id);
}
