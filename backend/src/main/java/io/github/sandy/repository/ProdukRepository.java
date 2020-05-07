package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.Produk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ProdukRepository extends JpaRepository<Produk, Integer> {

    Boolean existsByKodeProdukAndKoperasi(String kodeProduk, Koperasi koperasi);

    Produk getFirstByKodeProdukAndKoperasi(String kodeProduk, Koperasi koperasi);

    Set<Produk> getAllByKoperasi(Koperasi koperasi);

}
