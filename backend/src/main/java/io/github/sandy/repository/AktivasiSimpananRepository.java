package io.github.sandy.repository;

import io.github.sandy.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface AktivasiSimpananRepository extends JpaRepository<AktivasiSimpanan, Integer> {
    Boolean existsByKoperasiAndUserAndJenisSimpanan(Koperasi koperasi, User user, Integer jenisSimpanan);

    List<AktivasiSimpanan> findAllByKoperasi(Koperasi koperasi);

    AktivasiSimpanan getFirstByUserAndJenisSimpanan(User user, Integer jenisSimpanan);

    @Query(value = "SELECT sum(total_simpanan) from aktivasi_simpanan " +
            "WHERE id_user = ?1",
            nativeQuery = true)
    Long getSaldo(Integer idUser);

    List<AktivasiSimpanan> getAllByUser(User user);
}
