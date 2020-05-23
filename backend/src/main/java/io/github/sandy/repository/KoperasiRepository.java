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
            value = "SELECT nama_koperasi, koperasi.id, jenis_koperasi, nama_pendiri, alamat_koperasi, " +
                    "tahun_berdiri_koperasi, koperasi.email, no_izin_koperasi, users.have_koperasi,logo_koperasi FROM koperasi " +
                    "INNER JOIN users " +
                    "ON users.id = koperasi.id_user " +
                    "WHERE users.have_koperasi != 0",
            nativeQuery = true
    )
    List<Map<String, Object>> findByIsHaveKoperasi();

    @Query(
            value = "SELECT koperasi.id,nama_koperasi, alamat_koperasi, tahun_berdiri_koperasi, no_izin_koperasi, nama_pendiri, logo_koperasi, jenis_koperasi,email, have_field_register_member FROM koperasi " +
                    "inner join anggota_koperasi ak on koperasi.id = ak.id_koperasi " +
                    "WHERE  ak.id_user= ?1 LIMIT 1",
            nativeQuery = true
    )
    Map<String, Object> getKoperasiUserIdInnerAnggota(Integer id);

    @Query(
            value = "SELECT id, nama_koperasi, alamat_koperasi, tahun_berdiri_koperasi, no_izin_koperasi, nama_pendiri, logo_koperasi, jenis_koperasi, id_user, email, have_field_register_member FROM koperasi " +
                    "WHERE id_user = ?1 LIMIT 1",
            nativeQuery = true
    )
    Map<String, Object> getKoperasiUserId(Integer id);
    @Query(
            value = "SELECT id, nama_koperasi, alamat_koperasi, tahun_berdiri_koperasi, no_izin_koperasi, nama_pendiri, logo_koperasi, jenis_koperasi, id_user, email, have_field_register_member FROM koperasi " +
                    "WHERE id = ?1 LIMIT 1",
            nativeQuery = true
    )
    Map<String, Object> getKoperasiID(Integer id);

    @Query(
            value = "SELECT jenis_koperasi from koperasi " +
                    "INNER JOIN users on koperasi.id_user = users.id " +
                    "where users.id = ?1 LIMIT 1",
            nativeQuery = true
    )
    Integer getJenisFromKoperasi(Integer idUser);

    @Query(
            value = "select k.id, nama_koperasi, alamat_koperasi, tahun_berdiri_koperasi, no_izin_koperasi, nama_pendiri, logo_koperasi, jenis_koperasi, have_field_register_member from koperasi k " +
                    "INNER JOIN aktivasi_simpanan a on k.id = a.id_koperasi " +
                    "where a.id = ?1 limit 1",
            nativeQuery = true
    )
    Map<String, Object> getKoperasibySimpanan(Integer idSimpanan);

    Koperasi findFirstByUser(User user);
}
