package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.TransaksiProduk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface TransaksiProdukRepository extends JpaRepository<TransaksiProduk, Integer> {

    @Query(value = "SELECT max(kode_transaksi) from transaksi_produk WHERE id_koperasi = ?1",
            nativeQuery = true)
    String getMaxKodePinjaman(Integer idKoperasi);

    @Query(value = "SELECT * from transaksi_produk " +
            " WHERE id_koperasi = ?1",
            nativeQuery = true)
    List<Map<String, Object>> getAllByKoperasi(Integer koperasi);
}
