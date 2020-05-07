package io.github.sandy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "produk_baru")
public class ProdukBaru implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "harga_beli")
    private Integer hargaBeli;

    @Column(name = "harga_jual_non_anggota")
    private Integer hargaJualNonAnggota;

    @Column(name = "harga_jual_anggota")
    private Integer hargaJualAnggota;

    @Column(name = "jumlah_produk")
    private Integer jumlahProduk;


    @JsonIgnoreProperties({"produkBaru", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_produk", nullable = false)
    private Produk produk;

    @JsonBackReference
    @JsonIgnoreProperties({"produkBaru", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vendor", nullable = false)
    private Vendor vendor;

    public ProdukBaru() {
    }

    public Integer getJumlahProduk() {
        return jumlahProduk;
    }

    public void setJumlahProduk(Integer jumlahProduk) {
        this.jumlahProduk = jumlahProduk;
    }

    public Integer getId() {
        return id;
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

    public Produk getProduk() {
        return produk;
    }

    public void setProduk(Produk produk) {
        this.produk = produk;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
}
