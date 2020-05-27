package io.github.sandy.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "laporan_koperasi")
public class LaporanKoperasi implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "path_laporan")
    private String pathLaporan;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "id_koperasi")
    private Integer idKoperasi;

    @Column(name = "tahun_laporan")
    private Integer tahunLaporan;

    @Column(name = "status")
    private Integer status;

    @Column(name = "extensi_file")
    private String extensiFile;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public String getExtensiFile() {
        return extensiFile;
    }

    public void setExtensiFile(String extensiFile) {
        this.extensiFile = extensiFile;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPathLaporan() {
        return pathLaporan;
    }

    public void setPathLaporan(String pathLaporan) {
        this.pathLaporan = pathLaporan;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Integer getIdKoperasi() {
        return idKoperasi;
    }

    public void setIdKoperasi(Integer idKoperasi) {
        this.idKoperasi = idKoperasi;
    }

    public Integer getTahunLaporan() {
        return tahunLaporan;
    }

    public void setTahunLaporan(Integer tahunLaporan) {
        this.tahunLaporan = tahunLaporan;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
