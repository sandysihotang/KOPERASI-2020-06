package io.github.sandy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "angsuran")
public class Angsuran implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "angsuran_pokok")
    private Double angsuranPokok;

    @Column(name = "bunga")
    private Double bunga;

    @Column(name = "status_bayar")
    private boolean statusBayar;

    @Column(name = "tanggal_bayar")
    @Nullable
    private Date tanggalBayar;

    @Column(name = "tanggal_jatuh_tempo")
    private Date tanggalJatuhTempo;

    @Column(name = "denda")
    @Nullable
    private Double denda;

    @Column(name = "total_bayar")
    @Nullable
    private Double totalBayar;

    @Column(name = "total_angsuran")
    private Double totalAngsuran;

    @Column(name = "total_tagihan")
    private Double totalTagihan;

    @Column(name = "urutan_ke")
    private Integer urutanKe;

    @JsonIgnoreProperties({"angsuran", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pinjaman", nullable = false)
    private Pinjaman pinjaman;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAngsuranPokok() {
        return angsuranPokok;
    }

    public void setAngsuranPokok(Double angsuranPokok) {
        this.angsuranPokok = angsuranPokok;
    }

    public Double getBunga() {
        return bunga;
    }

    public Date getTanggalJatuhTempo() {
        return tanggalJatuhTempo;
    }

    public void setTanggalJatuhTempo(Date tanggalJatuhTempo) {
        this.tanggalJatuhTempo = tanggalJatuhTempo;
    }

    public void setBunga(Double bunga) {
        this.bunga = bunga;
    }

    public boolean isStatusBayar() {
        return statusBayar;
    }

    public void setStatusBayar(boolean statusBayar) {
        this.statusBayar = statusBayar;
    }

    public Date getTanggalBayar() {
        return tanggalBayar;
    }

    public void setTanggalBayar(Date tanggalBayar) {
        this.tanggalBayar = tanggalBayar;
    }

    public Double getDenda() {
        return denda;
    }

    public void setDenda(Double denda) {
        this.denda = denda;
    }

    public Double getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(Double totalBayar) {
        this.totalBayar = totalBayar;
    }

    public Double getTotalAngsuran() {
        return totalAngsuran;
    }

    public void setTotalAngsuran(Double totalAngsuran) {
        this.totalAngsuran = totalAngsuran;
    }

    public Double getTotalTagihan() {
        return totalTagihan;
    }

    public void setTotalTagihan(Double totalTagihan) {
        this.totalTagihan = totalTagihan;
    }

    public Integer getUrutanKe() {
        return urutanKe;
    }

    public void setUrutanKe(Integer urutanKe) {
        this.urutanKe = urutanKe;
    }

    public Pinjaman getPinjaman() {
        return pinjaman;
    }

    public void setPinjaman(Pinjaman pinjaman) {
        this.pinjaman = pinjaman;
    }
}
