package io.github.sandy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "aktivasi_simpanan")
public class AktivasiSimpanan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "aktif")
    private Boolean aktif;

    @Column(name = "jenis_simpanan")
    private Integer jenisSimpanan;

    @Column(name = "total_simpanan")
    private Long totalSimpanan;

    @Column(name = "id_koperasi")
    private Integer idKoperasi;

    @Column(name = "id_user")
    private Integer idUser;

    @JsonIgnoreProperties({"aktivasiSimpanan", "hibernateLazyInitializer", "handler"})
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "aktivasiSimpanan")
    private Set<TransaksiSimpanan> transaksiSimpanan = new HashSet<>();

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "tanggal_mulai")
    private Date tanggalMulai;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public AktivasiSimpanan() {
    }

    public Date getTanggalMulai() {
        return tanggalMulai;
    }

    public void setTanggalMulai(Date tanggalMulai) {
        this.tanggalMulai = tanggalMulai;
    }

    public Set<TransaksiSimpanan> getTransaksiSimpanan() {
        return transaksiSimpanan;
    }

    public void setTransaksiSimpanan(Set<TransaksiSimpanan> transaksiSimpanan) {
        this.transaksiSimpanan = transaksiSimpanan;
    }

    public Integer getId() {
        return id;
    }

    public Long getTotalSimpanan() {
        return totalSimpanan;
    }

    public void setTotalSimpanan(Long totalSimpanan) {
        this.totalSimpanan = totalSimpanan;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getAktif() {
        return aktif;
    }

    public void setAktif(Boolean aktif) {
        this.aktif = aktif;
    }

    public Integer getJenisSimpanan() {
        return jenisSimpanan;
    }

    public void setJenisSimpanan(Integer jenisSimpanan) {
        this.jenisSimpanan = jenisSimpanan;
    }

    public Integer getIdKoperasi() {
        return idKoperasi;
    }

    public void setIdKoperasi(Integer idKoperasi) {
        this.idKoperasi = idKoperasi;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
}
