package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface KoperasiRepository extends JpaRepository<Koperasi, Integer> {
    @Query(
            value = "SELECT * FROM koperasi " +
                    "INNER JOIN users " +
                    "ON users.id = koperasi.id_user " +
                    "WHERE users.have_koperasi != 0",
            nativeQuery = true
    )
    List<Koperasi> findByIsHaveKoperasi();

    @Query(
            value = "SELECT id, nama_koperasi, alamat_koperasi, tahun_berdiri_koperasi, no_izin_koperasi, nama_pendiri, logo_koperasi, jenis_koperasi, id_user, email, have_field_register_member FROM koperasi " +
                    "LIMIT 1",
            nativeQuery = true
    )
    Map<String, Object> getKoperasiUserId(Integer id);

    @Query(
            value = "SELECT jenis_koperasi from koperasi " +
                    "INNER JOIN users on koperasi.id_user = users.id " +
                    "where users.id = ?1 LIMIT 1",
            nativeQuery = true
    )
    Integer getJenisFromKoperasi(Integer idUser);

    Koperasi findFirstByUser(User user);
}
