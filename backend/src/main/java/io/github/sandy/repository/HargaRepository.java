package io.github.sandy.repository;

import io.github.sandy.model.Harga;
import io.github.sandy.model.Produk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HargaRepository extends JpaRepository<Harga, Integer> {

    Boolean existsByProdukAndStatus(Produk produk, Boolean status);

    List<Harga> getAllByProdukAndStatus(Produk produk, Boolean status);

    Harga getFirstByProdukAndStatus(Produk produk, Boolean status);
}
