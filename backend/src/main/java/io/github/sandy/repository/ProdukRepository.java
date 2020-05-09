package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.Produk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ProdukRepository extends JpaRepository<Produk, Integer> {

    Boolean existsByKodeProdukAndKoperasi(String kodeProduk, Koperasi koperasi);

    Produk getFirstByKodeProdukAndKoperasi(String kodeProduk, Koperasi koperasi);

    Set<Produk> getAllByKoperasi(Koperasi koperasi);

    @Query(value = "SELECT sum(jumlah_produk) from produk where id_koperasi = ?1",
            nativeQuery = true)
    Integer getTotalProduk(Integer idKoperasi);

    Boolean existsByKoperasi(Koperasi koperasi);
}
