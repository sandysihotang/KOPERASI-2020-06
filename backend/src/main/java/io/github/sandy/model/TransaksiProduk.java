package io.github.sandy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "transaksi_produk")
public class TransaksiProduk implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "kode_transaksi")
    private String kodeTransaksi;

    @Column(name = "keanggotaan")
    private Boolean keanggotaan;

    @Column(name = "uang_masuk")
    private Integer uangMasuk;

    @Column(name = "tanggal_transaksi")
    private Date tanggalTransaksi;

    @Column(name = "id_koperasi")
    private Integer idKoperasi;

    @JsonManagedReference
    @JsonIgnoreProperties({"transaksiProduk", "hibernateLazyInitializer", "handler"})
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "transaksiProduk")
    private Set<PenjualanProduk> penjualanProduk = new HashSet<>();


    public TransaksiProduk() {
    }

    public Boolean getKeanggotaan() {
        return keanggotaan;
    }

    public void setKeanggotaan(Boolean keanggotaan) {
        this.keanggotaan = keanggotaan;
    }

    public Integer getUangMasuk() {
        return uangMasuk;
    }

    public void setUangMasuk(Integer uangMasuk) {
        this.uangMasuk = uangMasuk;
    }

    public Set<PenjualanProduk> getPenjualanProduk() {
        return penjualanProduk;
    }

    public void setPenjualanProduk(Set<PenjualanProduk> penjualanProduk) {
        this.penjualanProduk = penjualanProduk;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKodeTransaksi() {
        return kodeTransaksi;
    }

    public void setKodeTransaksi(String kodeTransaksi) {
        this.kodeTransaksi = kodeTransaksi;
    }

    public Date getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(Date tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }

    public Integer getIdKoperasi() {
        return idKoperasi;
    }

    public void setIdKoperasi(Integer idKoperasi) {
        this.idKoperasi = idKoperasi;
    }
}
