package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.PenjualanProduk;
import io.github.sandy.model.TransaksiProduk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PenjualanProdukRepository extends JpaRepository<PenjualanProduk, Integer> {
    List<PenjualanProduk> findAllByKoperasiAndStatus(Koperasi koperasi, Boolean status);

    List<PenjualanProduk> findAllByTransaksiProduk(TransaksiProduk transaksiproduk);

    Boolean existsByKoperasiAndStatus(Koperasi koperasi, Boolean status);

    @Query(value = "SELECT count(jumlah_beli) FROM penjualan_produk where id_koperasi = ?1 and status = ?2",
            nativeQuery = true)
    Integer getJumlahTerjual(Integer idKoperasi, Boolean status);

    @Query(value = "SELECT sum(p.jumlah_beli * h.harga_jual_anggota) FROM penjualan_produk p " +
            "inner join harga h " +
            "on p.id_harga = h.id " +
            "WHERE p.id_koperasi = ?1 AND p.keanggotaan = true AND p.status = true",
            nativeQuery = true)
    Integer getJumlahUangTerjualAnggota(Integer idKoperasi);

    @Query(value = "SELECT sum(p.jumlah_beli * h.harga_jual_non_anggota) FROM penjualan_produk p " +
            "inner join harga h " +
            "on p.id_harga = h.id " +
            "WHERE p.id_koperasi = ?1 AND p.keanggotaan = false AND p.status = true",
            nativeQuery = true)
    Integer getJumlahUangTerjualNonAnggota(Integer idKoperasi);
}
