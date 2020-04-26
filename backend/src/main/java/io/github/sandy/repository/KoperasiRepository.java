package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KoperasiRepository extends JpaRepository<Koperasi,Integer> {
    @Query(
            value = "SELECT * FROM koperasi " +
                    "INNER JOIN user " +
                    "ON user.id = koperasi.id_user " +
                    "WHERE user.haveKoperasi != 0 " ,
            nativeQuery = true
    )
    List<Koperasi> findByIsHaveKoperasi();


    Koperasi findFirstByUser(User user);
}
