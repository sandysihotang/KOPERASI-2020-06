package io.github.sandy.repository;

import io.github.sandy.model.KategoriProduk;
import io.github.sandy.model.Koperasi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KategoriProdukRepository extends JpaRepository<KategoriProduk, Integer> {

    List<KategoriProduk> getAllByKoperasi(Koperasi koperasi);

}
