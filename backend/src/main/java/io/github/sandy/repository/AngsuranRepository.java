package io.github.sandy.repository;

import io.github.sandy.model.Angsuran;
import io.github.sandy.model.Pinjaman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AngsuranRepository extends JpaRepository<Angsuran, Integer> {

    List<Angsuran> getAllByPinjamanOrderByUrutanKeAsc(Pinjaman pinjaman);

    List<Angsuran> getAllByPinjamanAndStatusBayar(Pinjaman pinjaman, Boolean status);

    Angsuran getFirstByPinjamanAndStatusBayarOrderByUrutanKe(Pinjaman pinjaman, Boolean status);

    Boolean existsByPinjamanAndStatusBayar(Pinjaman pinjaman, Boolean statusBayar);

    @Query(value = "SELECT * FROM angsuran " +
            "INNER JOIN peminjaman on angsuran.id_pinjaman = peminjaman.id " +
            "INNER JOIN pengaturan_pinjaman on peminjaman.id_pengaturan_pinjaman = pengaturan_pinjaman.id " +
            "WHERE angsuran.status_bayar = false AND angsuran.tanggal_jatuh_tempo < CURRENT_DATE", nativeQuery = true)
    List<Angsuran> getExistDenda();
}
