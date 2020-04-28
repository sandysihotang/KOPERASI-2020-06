package io.github.sandy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "pengaturan_pinjaman")
public class PengaturanPinjaman implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "bunga_pinjaman")
    private Double bungaPinjaman;

    @Column(name = "min_tenor")
    private Integer minTenor;

    @Column(name = "max_tenor")
    private Integer maxTenor;

    @Column(name = "ambang_batas_denda")
    private Integer ambangBatasDenda;

    @Column(name = "persentase_denda")
    private Double persentaseDenda;

    @JsonManagedReference
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "pengaturanPinjaman", fetch = FetchType.EAGER)
    private Set<KoperasiPengaturanPinjaman> koperasiPengaturanPinjaman;

    @JsonIgnoreProperties({"pengaturan_pinjaman", "hibernateLazyInitializer", "handler"})
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "pengaturanPinjaman", fetch = FetchType.EAGER)
    private Set<Pinjaman> pinjaman = new HashSet<>();

    public PengaturanPinjaman() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getBungaPinjaman() {
        return bungaPinjaman;
    }

    public Set<KoperasiPengaturanPinjaman> getKoperasiPengaturanPinjaman() {
        return koperasiPengaturanPinjaman;
    }

    public void setKoperasiPengaturanPinjaman(Set<KoperasiPengaturanPinjaman> koperasiPengaturanPinjaman) {
        this.koperasiPengaturanPinjaman = koperasiPengaturanPinjaman;
    }

    public Set<Pinjaman> getPinjaman() {
        return pinjaman;
    }

    public void setPinjaman(Set<Pinjaman> pinjaman) {
        this.pinjaman = pinjaman;
    }

    public void setBungaPinjaman(Double bungaPinjaman) {
        this.bungaPinjaman = bungaPinjaman;
    }

    public Integer getMinTenor() {
        return minTenor;
    }

    public void setMinTenor(Integer minTenor) {
        this.minTenor = minTenor;
    }

    public Integer getMaxTenor() {
        return maxTenor;
    }

    public void setMaxTenor(Integer maxTenor) {
        this.maxTenor = maxTenor;
    }

    public Integer getAmbangBatasDenda() {
        return ambangBatasDenda;
    }

    public void setAmbangBatasDenda(Integer ambangBatasDenda) {
        this.ambangBatasDenda = ambangBatasDenda;
    }

    public Double getPersentaseDenda() {
        return persentaseDenda;
    }

    public void setPersentaseDenda(Double persentaseDenda) {
        this.persentaseDenda = persentaseDenda;
    }
}
