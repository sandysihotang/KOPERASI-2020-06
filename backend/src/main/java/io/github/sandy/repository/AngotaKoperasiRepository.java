package io.github.sandy.repository;

import io.github.sandy.model.AnggotaKoperasi;
import io.github.sandy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface AngotaKoperasiRepository extends JpaRepository<AnggotaKoperasi, Integer> {
    Optional<AnggotaKoperasi> findFirstByUser(User user);

    @Query(value = "SELECT id, id_koperasi, data, id_user from anggota_koperasi where id_user = ?1 LIMIT 1",
            nativeQuery = true)
    Map<String, Object> getByFirstByIdUser(Integer idUser);

    @Query(value = "SELECT id, id_koperasi, data, id_user from anggota_koperasi where id_user = ?1",
            nativeQuery = true)
    Set<Map<String, Object>> getByUser(Integer user);

    @Query(value = "SELECT k.nama_koperasi from anggota_koperasi " +
            "INNER JOIN koperasi k on anggota_koperasi.id_koperasi = k.id where anggota_koperasi.id = ?1 LIMIT 1",
            nativeQuery = true)
    String getNameKoperasi(Integer id);

    @Query(value = "SELECT id, id_koperasi, data, id_user from anggota_koperasi where id_user = ?1 LIMIT 1",
            nativeQuery = true)
    Map<String, Object> getFirstByUser(Integer user);

    @Query(value = "SELECT count(u.id) from anggota_koperasi a " +
            "INNER JOIN users u " +
            "ON u.id = a.id_user " +
            "WHERE a.id_koperasi = ?1 AND u.enabled = ?2",
            nativeQuery = true)
    Integer getCountJumlahAnggota(Integer idKoperasi, Boolean status);

    @Query(value = "SELECT a.data, a.id FROM anggota_koperasi a " +
            "INNER JOIN koperasi k ON a.id_koperasi = k.id " +
            "INNER JOIN users u ON a.id_user = u.id " +
            "WHERE a.id_koperasi = ?1 AND u.enabled = ?2",
            nativeQuery = true)
    Set<Map<String, Object>> getALlAnggotaKoperasiEnable(Integer idKoperasi, Boolean status);

    @Query(value = "SELECT count(*) FROM anggota_koperasi a " +
            "INNER JOIN koperasi k ON a.id_koperasi = k.id " +
            "INNER JOIN users u ON a.id_user = u.id " +
            "WHERE a.id_koperasi = ?1 AND u.enabled = ?2",
            nativeQuery = true)
    Long getCountAnggota(Integer idKoperasi, Boolean status);
}
