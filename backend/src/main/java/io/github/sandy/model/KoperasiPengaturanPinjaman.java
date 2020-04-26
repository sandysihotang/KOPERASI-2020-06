package io.github.sandy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "koperasi_pengaturan_pinjaman")
public class KoperasiPengaturanPinjaman implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_koperasi", nullable = false)
    private Koperasi koperasi;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pengaturan", nullable = false)
    private PengaturanPinjaman pengaturanPinjaman;

    @Column(name = "status")
    private boolean status;

    public KoperasiPengaturanPinjaman() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Koperasi getKoperasi() {
        return koperasi;
    }

    public void setKoperasi(Koperasi koperasi) {
        this.koperasi = koperasi;
    }

    public PengaturanPinjaman getPengaturanPinjaman() {
        return pengaturanPinjaman;
    }

    public void setPengaturanPinjaman(PengaturanPinjaman pengaturanPinjaman) {
        this.pengaturanPinjaman = pengaturanPinjaman;
    }
}
