package io.github.sandy.repository;

import io.github.sandy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String s);

    @Query(
            value = "SELECT * FROM users " +
                    "INNER JOIN role_user " +
                    "ON users.id = role_user.user_id " +
                    "WHERE users.enabled = false " +
                    "AND role_user.role_id = 2",
            nativeQuery = true
    )
    List<User> findByRole();


    @Query(value = "SELECT * FROM users u " +
            "INNER JOIN anggota_koperasi a ON u.id = a.id_user " +
            "WHERE a.id_koperasi = ?1", nativeQuery = true)
    List<User> findAllForPengaju(Integer idKoperasi);

    @Query(value = "SELECT * FROM users u " +
            "INNER JOIN aktivasi_simpanan a ON u.id = a.id_user " +
            "WHERE a.id_koperasi = ?1 AND a.jenis_simpanan= ?2", nativeQuery = true)
    List<User> findAllByJenisSimpanan(Integer idKoperasi, Integer jenisSimpanan);

    @Query(value = "SELECT * FROM users u " +
            "INNER JOIN anggota_koperasi a ON u.id = a.id_user " +
            "WHERE a.id_koperasi = ?1 AND u.enabled = true", nativeQuery = true)
    List<User> findByKoperasiForRequest(Integer koperasi, Integer[] i);

    @Query(value = "SELECT * FROM users u INNER JOIN anggota_koperasi ak on u.id = ak.id_user WHERE ak.id_koperasi = ?1 and u.enabled = true", nativeQuery = true)
    List<User> findByKoperasiforReport(Integer koperasi);

    User findFirstById(Integer id);
}
