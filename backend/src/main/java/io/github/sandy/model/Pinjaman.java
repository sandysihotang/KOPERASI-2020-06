package io.github.sandy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "peminjaman")
public class Pinjaman implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "jumlah_pinjaman")
    private Double jumlahPinjaman;

    @Column(name = "jaminan")
    private String jaminan;

    //    @JsonBackReference
    @JsonIgnoreProperties({"pinjaman", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    //    @JsonBackReference
    @JsonIgnoreProperties({"pinjaman", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pengaturan_pinjaman", nullable = false)
    private PengaturanPinjaman pengaturanPinjaman;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_koperasi", nullable = false)
    private Koperasi koperasi;

    @Column(name = "status")
    private Integer status;

    @Column(name = "kode_pinjaman")
    private String kodePinjaman;

    @Column(name = "tenor")
    private Integer tenor;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

    public Pinjaman() {
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getKodePinjaman() {
        return kodePinjaman;
    }

    public Koperasi getKoperasi() {
        return koperasi;
    }

    public void setKoperasi(Koperasi koperasi) {
        this.koperasi = koperasi;
    }

    public void setKodePinjaman(String kodePinjaman) {
        this.kodePinjaman = kodePinjaman;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Integer getTenor() {
        return tenor;
    }

    public void setTenor(Integer tenor) {
        this.tenor = tenor;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getJumlahPinjaman() {
        return jumlahPinjaman;
    }

    public void setJumlahPinjaman(Double jumlahPinjaman) {
        this.jumlahPinjaman = jumlahPinjaman;
    }

    public String getJaminan() {
        return jaminan;
    }

    public void setJaminan(String jaminan) {
        this.jaminan = jaminan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PengaturanPinjaman getPengaturanPinjaman() {
        return pengaturanPinjaman;
    }

    public void setPengaturanPinjaman(PengaturanPinjaman pengaturanPinjaman) {
        this.pengaturanPinjaman = pengaturanPinjaman;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}