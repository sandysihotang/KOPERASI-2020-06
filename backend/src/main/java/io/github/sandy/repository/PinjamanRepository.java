package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.Pinjaman;
import io.github.sandy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface PinjamanRepository extends JpaRepository<Pinjaman, Integer> {
    Boolean existsByUserAndStatusIn(User user, Integer[] status);

    Pinjaman getFirstByUserAndStatusIn(User user, Integer[] status);

    List<Pinjaman> getByUserAndStatus(User user, Integer status);


    @Query(value = "SELECT max(kode_pinjaman) from peminjaman WHERE id_koperasi = ?1",
            nativeQuery = true)
    String getMaxKodePinjaman(Integer idKoperasi);

    @Query(value = "SELECT sum(jumlah_pinjaman) from peminjaman WHERE id_koperasi = ?1 AND status = ?2",
            nativeQuery = true)
    Integer getPinjaman(Integer idKoperasi, Integer status);

    Boolean existsByKoperasiAndStatus(Koperasi koperasi, Integer status);

    List<Pinjaman> getAllByKoperasi(Koperasi koperasi);

    List<Pinjaman> getAllByKoperasiAndStatus(Koperasi koperasi, Integer status);

    Pinjaman getFirstById(int id);

    Boolean existsByUserAndStatus(User user, int status);

    Pinjaman getFirstByUserAndStatus(User user, int status);
}
