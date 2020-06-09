package io.github.sandy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vendor")
public class Vendor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nama_vendor")
    private String namaVendor;

    @Column(name = "no_telepon")
    private String noTelepon;

    @Column(name = "alamat_vendor")
    private String alamatVendor;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "id_koperasi")
    private Integer idKoperasi;

    @JsonManagedReference
    @JsonIgnoreProperties({"vendor", "hibernateLazyInitializer", "handler"})
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "vendor", fetch = FetchType.EAGER)
    private Set<ProdukBaru> produkBaru = new HashSet<>();

    @Column(name = "tanggal_masuk")
    private Date tanggalMasuk;

    public Vendor() {
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Set<ProdukBaru> getProdukBaru() {
        return produkBaru;
    }

    public void setProdukBaru(Set<ProdukBaru> produkBaru) {
        this.produkBaru = produkBaru;
    }

    public Date getTanggalMasuk() {
        return tanggalMasuk;
    }

    public void setTanggalMasuk(Date tanggalMasuk) {
        this.tanggalMasuk = tanggalMasuk;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamaVendor() {
        return namaVendor;
    }

    public void setNamaVendor(String namaVendor) {
        this.namaVendor = namaVendor;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getAlamatVendor() {
        return alamatVendor;
    }

    public void setAlamatVendor(String alamatVendor) {
        this.alamatVendor = alamatVendor;
    }

    public Integer getIdKoperasi() {
        return idKoperasi;
    }

    public void setIdKoperasi(Integer idKoperasi) {
        this.idKoperasi = idKoperasi;
    }
}
