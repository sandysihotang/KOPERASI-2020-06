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
@Table(name = "kategori_produk")
public class KategoriProduk implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nama_kategori")
    private String namaKategori;

    @JsonBackReference
    @JsonIgnoreProperties({"kategoriProduk", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_koperasi", nullable = false)
    private Koperasi koperasi;


    @JsonIgnoreProperties({"kategoriProduk", "hibernateLazyInitializer", "handler"})
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "kategoriProduk")
    private Set<Produk> produk = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }

    public Koperasi getKoperasi() {
        return koperasi;
    }

    public void setKoperasi(Koperasi koperasi) {
        this.koperasi = koperasi;
    }

    public Set<Produk> getProduk() {
        return produk;
    }

    public void setProduk(Set<Produk> produk) {
        this.produk = produk;
    }
}