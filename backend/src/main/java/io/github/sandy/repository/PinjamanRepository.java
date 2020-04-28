package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.Pinjaman;
import io.github.sandy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;


public interface PinjamanRepository extends JpaRepository<Pinjaman, Integer> {
    Boolean existsByUserAndStatusNot(User user, Integer status);

    Pinjaman getFirstByUserAndStatusNot(User user, Integer status);


    @Query(value = "SELECT max(kode_pinjaman) from peminjaman",
            nativeQuery = true)
    String getMaxKodePinjaman();


    List<Pinjaman> getAllByKoperasi(Koperasi koperasi);
    Pinjaman getFirstById(int id);
}
