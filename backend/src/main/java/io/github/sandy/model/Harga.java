package io.github.sandy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "harga")
public class Harga implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "harga_beli")
    private Integer hargaBeli;

    @Column(name = "harga_jual_non_anggota")
    private Integer hargaJualNonAnggota;

    @Column(name = "harga_jual_anggota")
    private Integer hargaJualAnggota;

    @Column(name = "status")
    private Boolean status;

    @JsonIgnoreProperties({"harga", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_produk", nullable = false)
    private Produk produk;

    @JsonIgnoreProperties({"harga", "hibernateLazyInitializer", "handler"})
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "harga")
    private Set<PenjualanProduk> penjualanProduk = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public Set<PenjualanProduk> getPenjualanProduk() {
        return penjualanProduk;
    }

    public void setPenjualanProduk(Set<PenjualanProduk> penjualanProduk) {
        this.penjualanProduk = penjualanProduk;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHargaBeli() {
        return hargaBeli;
    }

    public void setHargaBeli(Integer hargaBeli) {
        this.hargaBeli = hargaBeli;
    }

    public Integer getHargaJualNonAnggota() {
        return hargaJualNonAnggota;
    }

    public void setHargaJualNonAnggota(Integer hargaJualNonAnggota) {
        this.hargaJualNonAnggota = hargaJualNonAnggota;
    }

    public Integer getHargaJualAnggota() {
        return hargaJualAnggota;
    }

    public void setHargaJualAnggota(Integer hargaJualAnggota) {
        this.hargaJualAnggota = hargaJualAnggota;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Produk getProduk() {
        return produk;
    }

    public void setProduk(Produk produk) {
        this.produk = produk;
    }
}
