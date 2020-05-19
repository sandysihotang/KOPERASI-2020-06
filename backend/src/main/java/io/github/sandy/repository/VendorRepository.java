package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;


public interface VendorRepository extends JpaRepository<Vendor, Integer> {

    @Query(value = "SELECT * from vendor where id_koperasi = ?1 and status = ?2",
            nativeQuery = true)
    List<Map<String, Object>> getAllByKoperasiAndStatusOrderByTanggalMasukDesc(Integer koperasi, Boolean status);
}
