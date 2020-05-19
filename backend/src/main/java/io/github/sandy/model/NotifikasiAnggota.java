package io.github.sandy.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "notifikasi_anggota")
public class NotifikasiAnggota implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pesan")
    private String pesan;

    @Column(name = "tanggal_notifikasi")
    private Date tanggalNotifikasi;

    @Column(name = "id_angsuran")
    private Integer idAngsuran;

    @Column(name = "id_user")
    private Integer idUser;

    @Column(name = "status")
    private Boolean status;

    public Integer getId() {
        return id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

    public Date getTanggalNotifikasi() {
        return tanggalNotifikasi;
    }

    public void setTanggalNotifikasi(Date tanggalNotifikasi) {
        this.tanggalNotifikasi = tanggalNotifikasi;
    }

    public Integer getIdAngsuran() {
        return idAngsuran;
    }

    public void setIdAngsuran(Integer idAngsuran) {
        this.idAngsuran = idAngsuran;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
}
