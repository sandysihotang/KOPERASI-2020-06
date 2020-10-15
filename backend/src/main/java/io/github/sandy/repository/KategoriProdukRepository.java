package io.github.sandy.repository;

import io.github.sandy.model.KategoriProduk;
import io.github.sandy.model.Koperasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface KategoriProdukRepository extends JpaRepository<KategoriProduk, Integer> {

    @Query(value = "SELECT * from kategori_produk where id_koperasi = ?1",
            nativeQuery = true)
    List<Map<String, Object>> getAllByKoperasi(Integer koperasi);

}
