package io.github.sandy.repository;

import io.github.sandy.model.ProdukBaru;
import io.github.sandy.model.TransaksiSimpanan;
import io.github.sandy.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProdukBaruRepository extends JpaRepository<ProdukBaru, Integer> {
    List<ProdukBaru> getAllByVendor(Vendor vendor);

    @Query(value = "SELECT pb.harga_beli, pb.harga_jual_non_anggota, pb.harga_jual_anggota, pb.jumlah_produk," +
            "p.nama_produk, p.kode_produk," +
            "kp.nama_kategori, v.tanggal_masuk from produk_baru pb " +
            "INNER JOIN produk p on pb.id_produk = p.id " +
            "INNER JOIN vendor v on pb.id_vendor = v.id " +
            "INNER JOIN kategori_produk kp on p.id_kategori = kp.id " +
            "WHERE v.id_koperasi = ?1 AND v.tanggal_masuk >= ?2 AND v.tanggal_masuk <= ?3 AND v.status = true",
            nativeQuery = true)
    List<Map<String, Object>> getTransaksiProdukMasukLaporan(Integer idKoperasi, Date from, Date to);

    @Query(value = "SELECT CASE WHEN " +
            "((SELECT count(*) from produk_baru pb " +
            "INNER JOIN produk p on pb.id_produk = p.id " +
            "INNER JOIN vendor v on pb.id_vendor = v.id " +
            "INNER JOIN kategori_produk kp on p.id_kategori = kp.id " +
            "WHERE v.id_koperasi = ?1 AND v.tanggal_masuk >= ?2 AND v.tanggal_masuk <= ?3 AND v.status = true)>0) " +
            "then " +
            "(SELECT sum(pb.jumlah_produk) from produk_baru pb " +
            "INNER JOIN produk p on pb.id_produk = p.id " +
            "INNER JOIN vendor v on pb.id_vendor = v.id " +
            "INNER JOIN kategori_produk kp on p.id_kategori = kp.id " +
            "WHERE v.id_koperasi = ?1 AND v.tanggal_masuk >= ?2 AND v.tanggal_masuk <= ?3 AND v.status = true) else 0 end",
            nativeQuery = true)
    Integer getTotalTransaksiProdukMasukLaporan(Integer idKoperasi, Date from, Date to);

}
