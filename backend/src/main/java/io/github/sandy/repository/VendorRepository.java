package io.github.sandy.repository;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    List<Vendor> getAllByKoperasiAndStatusOrderByTanggalMasukDesc(Koperasi koperasi, Boolean status);
}
