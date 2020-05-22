package io.github.sandy.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "notifikasi_koperasi")
public class NotifikasiKoperasi implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pesan")
    private String pesan;

    @Column(name = "id_laporan")
    private Integer idLaporan;

    @Column(name = "tanggal_pengiriman")
    private Date tanggalPengiriman;

    @Column(name = "id_koperasi")
    private Integer idKoperasi;

    @Column(name = "status")
    private Boolean status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public Integer getIdLaporan() {
        return idLaporan;
    }

    public void setIdLaporan(Integer idLaporan) {
        this.idLaporan = idLaporan;
    }

    public Date getTanggalPengiriman() {
        return tanggalPengiriman;
    }

    public void setTanggalPengiriman(Date tanggalPengiriman) {
        this.tanggalPengiriman = tanggalPengiriman;
    }

    public Integer getIdKoperasi() {
        return idKoperasi;
    }

    public void setIdKoperasi(Integer idKoperasi) {
        this.idKoperasi = idKoperasi;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
