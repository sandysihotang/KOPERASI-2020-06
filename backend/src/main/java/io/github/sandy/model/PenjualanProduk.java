package io.github.sandy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "penjualan_produk")
public class PenjualanProduk implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "keanggotaan")
    private Boolean keanggotaan;

    @Column(name = "jumlah_beli")
    private Integer jumlahBeli;

    @Column(name = "id_koperasi")
    private Integer idKoperasi;

    @JsonBackReference
    @JsonIgnoreProperties({"transaksiProduk", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_transaksi", nullable = false)
    private TransaksiProduk transaksiProduk;


    @JsonIgnoreProperties({"transaksiProduk", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_harga", nullable = false)
    private Harga harga;

    public PenjualanProduk() {
    }

    public Boolean getKeanggotaan() {
        return keanggotaan;
    }

    public void setKeanggotaan(Boolean keanggotaan) {
        this.keanggotaan = keanggotaan;
    }

    public Integer getJumlahBeli() {
        return jumlahBeli;
    }

    public void setJumlahBeli(Integer jumlahBeli) {
        this.jumlahBeli = jumlahBeli;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getIdKoperasi() {
        return idKoperasi;
    }

    public void setIdKoperasi(Integer idKoperasi) {
        this.idKoperasi = idKoperasi;
    }

    public Harga getHarga() {
        return harga;
    }

    public void setHarga(Harga harga) {
        this.harga = harga;
    }

    public TransaksiProduk getTransaksiProduk() {
        return transaksiProduk;
    }

    public void setTransaksiProduk(TransaksiProduk transaksiProduk) {
        this.transaksiProduk = transaksiProduk;
    }
}
