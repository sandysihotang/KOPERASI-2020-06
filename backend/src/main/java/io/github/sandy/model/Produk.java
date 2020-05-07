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
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "produk")
public class Produk implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nama_produk")
    private String namaProduk;

    @Column(name = "kode_produk")
    private String kodeProduk;

    @Column(name = "jumlah_produk")
    private Integer jumlahProduk;

    @JsonBackReference
    @JsonIgnoreProperties({"produk", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_koperasi", nullable = false)
    private Koperasi koperasi;


    @JsonIgnoreProperties({"produk", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_kategori", nullable = false)
    private KategoriProduk kategoriProduk;

    @JsonIgnoreProperties({"produk", "hibernateLazyInitializer", "handler"})
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "produk")
    private Set<Harga> harga = new HashSet<>();

    @JsonIgnoreProperties({"produk", "hibernateLazyInitializer", "handler"})
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "produk", fetch = FetchType.EAGER)
    private Set<ProdukBaru> produkBaru = new HashSet<>();

    public Produk() {
    }


    public Set<ProdukBaru> getProdukBaru() {
        return produkBaru;
    }

    public void setProdukBaru(Set<ProdukBaru> produkBaru) {
        this.produkBaru = produkBaru;
    }

    public Integer getId() {
        return id;
    }

    public Set<Harga> getHarga() {
        return harga;
    }

    public void setHarga(Set<Harga> harga) {
        this.harga = harga;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public String getKodeProduk() {
        return kodeProduk;
    }

    public void setKodeProduk(String kodeProduk) {
        this.kodeProduk = kodeProduk;
    }

    public Integer getJumlahProduk() {
        return jumlahProduk;
    }

    public void setJumlahProduk(Integer jumlahProduk) {
        this.jumlahProduk = jumlahProduk;
    }

    public Koperasi getKoperasi() {
        return koperasi;
    }

    public void setKoperasi(Koperasi koperasi) {
        this.koperasi = koperasi;
    }

    public KategoriProduk getKategoriProduk() {
        return kategoriProduk;
    }

    public void setKategoriProduk(KategoriProduk kategoriProduk) {
        this.kategoriProduk = kategoriProduk;
    }
}
