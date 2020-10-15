package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.KoperasiPengaturanPinjaman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;
import java.util.Optional;

public interface KoperasiPengaturanPinjamanRepository extends JpaRepository<KoperasiPengaturanPinjaman, Integer> {
    Optional<KoperasiPengaturanPinjaman> getFirstByKoperasiAndStatus(Koperasi koperasi, Boolean status);

    @Query(value = "SELECT id, id_koperasi, id_pengaturan, status from koperasi_pengaturan_pinjaman where id_koperasi = ?1 AND status = ?2 limit 1",
            nativeQuery = true)
    Map<String, Object> getFirstByKoperasiAndStatusInPengaturan(Integer koperasi, Boolean status);

    Boolean existsByKoperasiAndStatus(Koperasi koperasi, Boolean status);

    @Query(value = "SELECT case when (" +
            "SELECT count(*) from koperasi_pengaturan_pinjaman where id_koperasi = ?1 AND status = ?2" +
            ") > 0 then true else false end ",
            nativeQuery = true)
    Boolean existsByKoperasiAndStatusInPengaturan(Integer koperasi, Boolean status);
}

