package io.github.sandy.repository;

import io.github.sandy.model.PengaturanPinjaman;
import io.github.sandy.model.Pinjaman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;
import java.util.Optional;

public interface PengaturanPinjamanRepository extends JpaRepository<PengaturanPinjaman, Integer> {
    Optional<PengaturanPinjaman> findFirstById(int id);


    @Query(value = "select id, bunga_pinjaman, min_tenor, max_tenor, ambang_batas_denda, persentase_denda from pengaturan_pinjaman " +
            "WHERE id = ?1 LIMIT 1",
            nativeQuery = true)
    Map<String, Object> getFirstById(Integer id);

    @Query(value = "select pp.id,pp.ambang_batas_denda, pp.bunga_pinjaman, pp.max_tenor, pp.min_tenor, pp.persentase_denda from pengaturan_pinjaman pp " +
            "INNER JOIN peminjaman p on pp.id = p.id_pengaturan_pinjaman WHERE p.id=?1 LIMIT 1",
            nativeQuery = true)
    Map<String, Object> getFirstByPinjaman(Integer id);
}
