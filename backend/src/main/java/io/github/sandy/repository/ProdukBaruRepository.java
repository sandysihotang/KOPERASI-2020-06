package io.github.sandy.repository;

import io.github.sandy.model.ProdukBaru;
import io.github.sandy.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ProdukBaruRepository extends JpaRepository<ProdukBaru, Integer> {
    List<ProdukBaru> getAllByVendor(Vendor vendor);
}
