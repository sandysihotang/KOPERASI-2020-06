package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.Produk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProdukRepository extends JpaRepository<Produk, Integer> {

    Boolean existsByKodeProdukAndKoperasi(String kodeProduk, Koperasi koperasi);

    Produk getFirstByKodeProdukAndKoperasi(String kodeProduk, Koperasi koperasi);

    Set<Produk> getAllByKoperasi(Koperasi koperasi);

    @Query(value = "SELECT sum(jumlah_produk) from produk where id_koperasi = ?1",
            nativeQuery = true)
    Integer getTotalProduk(Integer idKoperasi);

    @Query(value = "SELECT case when (select count(*) from produk where id_koperasi = ?1) > 0 then true else false end",
            nativeQuery = true)
    Boolean existsByKoperasi(Integer koperasi);

    @Query(value = "SELECT p.nama_produk, p.jumlah_produk, p.kode_produk, " +
            "h.harga_beli, h.harga_jual_anggota, h.harga_jual_non_anggota, kp.nama_kategori FROM produk p " +
            "INNER JOIN harga h on p.id = h.id_produk " +
            "INNER JOIN kategori_produk kp on p.id_kategori = kp.id " +
            "WHERE p.id_koperasi = ?1 AND h.status = true",
            nativeQuery = true)
    List<Map<String, String>> getAllData(Integer idKoperasi);
}
