package io.github.sandy.repository;

import io.github.sandy.model.Harga;
import io.github.sandy.model.Produk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HargaRepository extends JpaRepository<Harga, Integer> {

    Boolean existsByIdProdukAndStatus(Integer produk, Boolean status);

    List<Harga> getAllByIdProdukAndStatus(Integer produk, Boolean status);

    Harga getFirstByIdProdukAndStatus(Integer produk, Boolean status);
}
