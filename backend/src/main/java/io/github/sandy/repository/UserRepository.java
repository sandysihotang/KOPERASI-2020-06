package io.github.sandy.repository;

import io.github.sandy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String s);

    @Query(
            value = "SELECT * FROM users " +
                    "WHERE username = ?1",
            nativeQuery = true
    )
    Optional<Map<String, Object>> getdataFromUname(String s);

    @Query(
            value = "SELECT no_telepon ,first_name, last_name, username, ud.address, users.email, users.id FROM users " +
                    "INNER JOIN role_user " +
                    "ON users.id = role_user.user_id " +
                    "INNER JOIN user_detail ud on users.id = ud.user_id " +
                    "WHERE users.enabled = false " +
                    "AND role_user.role_id = 2",
            nativeQuery = true
    )
    List<Map<String, Object>> findByRole();

    @Query(
            value = "SELECT id, email, enabled, account_non_expired, credentials_non_expired, account_non_locked, username, password, have_koperasi FROM users " +
                    "where username = ?1 LIMIT 1",
            nativeQuery = true
    )
    Map<String, Object> getUserUsername(String user);


    @Query(value = "SELECT u.id, data FROM users u " +
            "INNER JOIN anggota_koperasi a ON u.id = a.id_user " +
            "WHERE a.id_koperasi = ?1", nativeQuery = true)
    List<Map<String, Object>> findAllForPengaju(Integer idKoperasi);

    @Query(value = "SELECT " +
            "a.id as id_aktivasi, u.id, a.aktif, a.jenis_simpanan, a.total_simpanan, data FROM users u " +
            "INNER JOIN aktivasi_simpanan a ON u.id = a.id_user " +
            "INNER JOIN anggota_koperasi ak on u.id = ak.id_user " +
            "WHERE a.id_koperasi = ?1 AND a.jenis_simpanan= ?2", nativeQuery = true)
    List<Map<String, Object>> findAllByJenisSimpanan(Integer idKoperasi, Integer jenisSimpanan);

    @Query(value = "SELECT u.id, a.data FROM users u " +
            "INNER JOIN anggota_koperasi a ON u.id = a.id_user " +
            "WHERE a.id_koperasi = ?1 AND u.enabled = true", nativeQuery = true)
    List<Map<String, Object>> findByKoperasiForRequest(Integer koperasi, Integer[] i);

    @Query(value = "SELECT first_name, last_name, u.id, email, enabled, account_non_expired, credentials_non_expired, account_non_locked, username, password, have_koperasi FROM users u " +
            "INNER JOIN anggota_koperasi ak on u.id = ak.id_user " +
            "INNER JOIN user_detail ud on u.id = ud.user_id " +
            "WHERE ak.id_koperasi = ?1 and u.enabled = true", nativeQuery = true)
    List<Map<String, Object>> findByKoperasiforReport(Integer koperasi);

    @Query(value = "SELECT * from users where id = ?1 limit 1", nativeQuery = true)
    Map<String, Object> findFirstById(Integer id);

    @Modifying(clearAutomatically = true)
    @Query(value = "update users set have_koperasi= ?2 where id = ?1", nativeQuery = true)
    @Transactional
    void update(Integer id, Integer state);

    @Modifying(clearAutomatically = true)
    @Query(value = "update users set enabled= true where id = ?1", nativeQuery = true)
    @Transactional
    void setEnablbe(Integer id);

    @Modifying(clearAutomatically = true)
    @Query(value = "update users set have_koperasi= 2 where id = ?1", nativeQuery = true)
    @Transactional
    void sethaveKoperasi(Integer id);

    @Modifying(clearAutomatically = true)
    @Query(value = "update users set password= ?2 where id = ?1", nativeQuery = true)
    @Transactional
    void updateUser(Integer idUser, String encode);
}
