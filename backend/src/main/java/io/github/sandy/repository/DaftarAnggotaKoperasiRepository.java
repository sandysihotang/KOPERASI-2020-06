package io.github.sandy.repository;

import io.github.sandy.model.FieldDaftarAnggota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

public interface DaftarAnggotaKoperasiRepository extends JpaRepository<FieldDaftarAnggota, Integer> {
    @Query(value = "SELECT id, koperasi_id, pattern_field from field_daftar_anggota where koperasi_id = ?1 LIMIT 1",
            nativeQuery = true)
    Map<String, Object> findByKoperasiId(Integer id);

    @Modifying(clearAutomatically = true)
    @Query(value = "INSERT INTO field_daftar_anggota(koperasi_id, pattern_field) VALUES (?1,?2)", nativeQuery = true)
    @Transactional
    void createHaveFieldKoperasi(Integer idKoperasi, String Pattern);

}
