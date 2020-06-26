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

    @Column(name = "id_koperasi")
    private Integer idKoperasi;


    @JsonIgnoreProperties({"produk", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_kategori", nullable = false)
    private KategoriProduk kategoriProduk;



    public Produk() {
    }

    public Integer getId() {
        return id;
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

    public Integer getIdKoperasi() {
        return idKoperasi;
    }

    public void setIdKoperasi(Integer idKoperasi) {
        this.idKoperasi = idKoperasi;
    }

    public KategoriProduk getKategoriProduk() {
        return kategoriProduk;
    }

    public void setKategoriProduk(KategoriProduk kategoriProduk) {
        this.kategoriProduk = kategoriProduk;
    }
}
