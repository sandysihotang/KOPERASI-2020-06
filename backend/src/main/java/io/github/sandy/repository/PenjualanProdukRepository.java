package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.PenjualanProduk;
import io.github.sandy.model.TransaksiProduk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PenjualanProdukRepository extends JpaRepository<PenjualanProduk, Integer> {
    List<PenjualanProduk> findAllByKoperasiAndStatus(Koperasi koperasi,Boolean status);
    List<PenjualanProduk> findAllByTransaksiProduk(TransaksiProduk transaksiproduk);
}
