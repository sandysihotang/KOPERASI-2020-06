package io.github.sandy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "transaksi_simpanan")
public class TransaksiSimpanan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "jumlah_transaksi")
    private Integer jumlahTransaksi;

    @JsonIgnoreProperties({"transaksiSimpanan", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_aktivasi", nullable = false)
    private AktivasiSimpanan aktivasiSimpanan;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "kode_transaksi")
    private String kodeTransaksi;

    @Column(name = "jenis_transaksi")
    private Integer jenisTransaksi;

    public Integer getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getKodeTransaksi() {
        return kodeTransaksi;
    }

    public void setKodeTransaksi(String kodeTransaksi) {
        this.kodeTransaksi = kodeTransaksi;
    }

    public Integer getJenisTransaksi() {
        return jenisTransaksi;
    }

    public void setJenisTransaksi(Integer jenisTransaksi) {
        this.jenisTransaksi = jenisTransaksi;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJumlahTransaksi() {
        return jumlahTransaksi;
    }

    public void setJumlahTransaksi(Integer jumlahTransaksi) {
        this.jumlahTransaksi = jumlahTransaksi;
    }

    public AktivasiSimpanan getAktivasiSimpanan() {
        return aktivasiSimpanan;
    }

    public void setAktivasiSimpanan(AktivasiSimpanan aktivasiSimpanan) {
        this.aktivasiSimpanan = aktivasiSimpanan;
    }
}
