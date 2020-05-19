package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.PenjualanProduk;
import io.github.sandy.model.TransaksiProduk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PenjualanProdukRepository extends JpaRepository<PenjualanProduk, Integer> {
    List<PenjualanProduk> findAllByKoperasiAndStatus(Koperasi koperasi, Boolean status);

    @Query(value = "SELECT p.id, id_transaksi,  id_harga, keanggotaan, jumlah_beli," +
            " harga_beli, harga_jual_non_anggota, harga_jual_anggota, " +
            " nama_produk, kode_produk," +
            " nama_kategori from penjualan_produk p " +
            "INNER JOIN harga h on p.id_harga = h.id " +
            "INNER JOIN produk p2 on h.id_produk = p2.id " +
            "INNER JOIN kategori_produk kp on p2.id_kategori = kp.id " +
            "WHERE p.id_koperasi = ?1 AND p.status = ?2 AND h.status = true",
            nativeQuery = true)
    List<Map<String, Object>> findAllByKoperasiAndStatusPen(Integer koperasi, Boolean status);

    List<PenjualanProduk> findAllByTransaksiProduk(TransaksiProduk transaksiproduk);

    @Query(value = "select case when (SELECT count(*) FROM penjualan_produk where id_koperasi = ?1 and status = ?2) > 0 then true else false end",
            nativeQuery = true)
    Boolean existsByKoperasiAndStatus(Integer koperasi, Boolean status);

    @Query(value = "SELECT count(jumlah_beli) FROM penjualan_produk where id_koperasi = ?1 and status = ?2",
            nativeQuery = true)
    Integer getJumlahTerjual(Integer idKoperasi, Boolean status);

    @Query(value = "select case when (" +
            "SELECT count(*) FROM penjualan_produk p " +
            "WHERE p.id_koperasi = ?1 AND p.keanggotaan = true AND p.status = true" +
            ")>0 then " +
            "(SELECT sum(p.jumlah_beli * h.harga_jual_anggota) FROM penjualan_produk p " +
            "inner join harga h " +
            "on p.id_harga = h.id " +
            "WHERE p.id_koperasi = ?1 AND p.keanggotaan = true AND p.status = true) else 0 end",
            nativeQuery = true)
    Integer getJumlahUangTerjualAnggota(Integer idKoperasi);

    @Query(value = "select case when (" +
            "SELECT count(*) FROM penjualan_produk p " +
            "WHERE p.id_koperasi = ?1 AND p.keanggotaan = true AND p.status = false" +
            ") > 0 then " +
            "(SELECT sum(p.jumlah_beli * h.harga_jual_non_anggota) FROM penjualan_produk p " +
            "inner join harga h " +
            "on p.id_harga = h.id " +
            "WHERE p.id_koperasi = ?1 AND p.keanggotaan = false AND p.status = true) else 0 end",
            nativeQuery = true)
    Integer getJumlahUangTerjualNonAnggota(Integer idKoperasi);


    @Query(value = "SELECT pp.jumlah_beli," +
            "case when (tp.keanggotaan = true) then h.harga_jual_anggota else h.harga_jual_non_anggota end as harga_jual," +
            "tp.tanggal_transaksi," +
            "p.nama_produk, p.kode_produk, tp.kode_transaksi, kp.nama_kategori FROM penjualan_produk pp " +
            "INNER JOIN harga h on pp.id_harga = h.id " +
            "INNER JOIN transaksi_produk tp on pp.id_transaksi = tp.id " +
            "INNER JOIN produk p on h.id_produk = p.id " +
            "INNER JOIN kategori_produk kp on p.id_kategori = kp.id " +
            "where p.id_koperasi = ?1 AND tp.tanggal_transaksi >= ?2 AND tp.tanggal_transaksi <= ?3",
            nativeQuery = true)
    List<Map<String, Object>> getLaporanPenjualan(Integer idKoperasi, Date from, Date to);

    @Query(value = "SELECT case when " +
            "((" +
            "SELECT count(*) from penjualan_produk" +
            " INNER JOIN transaksi_produk tp on penjualan_produk.id_transaksi = tp.id " +
            "where tp.id_koperasi=?1 AND tp.tanggal_transaksi >= ?2 AND tp.tanggal_transaksi <= ?3" +
            ")>0) then (" +
            "SELECT sum(penjualan_produk.jumlah_beli) from penjualan_produk" +
            " INNER JOIN transaksi_produk tp on penjualan_produk.id_transaksi = tp.id " +
            "where tp.id_koperasi=?1 AND tp.tanggal_transaksi >= ?2 AND tp.tanggal_transaksi <= ?3) else 0 end", nativeQuery = true)
    Integer getTotalLaporanPenjualan(Integer idKoperasi, Date from, Date to);

    @Query(value = "SELECT case when " +
            "((" +
            "SELECT count(*) from penjualan_produk" +
            " INNER JOIN transaksi_produk tp on penjualan_produk.id_transaksi = tp.id " +
            "where tp.id_koperasi=?1 AND tp.tanggal_transaksi >= ?2 AND tp.tanggal_transaksi <= ?3" +
            ")>0) then (" +
            "SELECT sum(penjualan_produk.jumlah_beli * case when tp.keanggotaan =true then h.harga_jual_anggota else h.harga_jual_non_anggota end) from penjualan_produk" +
            " INNER JOIN transaksi_produk tp on penjualan_produk.id_transaksi = tp.id " +
            "INNER JOIN harga h on penjualan_produk.id_harga = h.id " +
            "where tp.id_koperasi=?1 AND tp.tanggal_transaksi >= ?2 AND tp.tanggal_transaksi <= ?3) else 0 end", nativeQuery = true)
    Integer getTotalJual(Integer idKoperasi, Date from, Date to);

    @Modifying
    @Transactional
    @Query(value = "delete from penjualan_produk where id_koperasi = ?1 and status = ?2", nativeQuery = true)
    void deleteAllByKoperasiAndStatus(Integer idKoperasi, Boolean status);
}
