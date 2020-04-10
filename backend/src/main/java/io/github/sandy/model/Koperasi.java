package io.github.sandy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "koperasi")
public class Koperasi implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nama_koperasi")
    private String namaKoperasi;

    @Column(name = "alamat_koperasi")
    private String alamatKoperasi;

    @Column(name = "tahun_berdiri_koperasi")
    private String tahunBerdiriKoperasi;

    @Column(name = "no_izin_koperasi")
    private String noIzinKoperasi;

    @Column(name = "nama_pendiri")
    private String namaPendiri;

    @Column(name = "logo_koperasi")
    private String logoKoperasi;

    @Column(name = "jenis_koperasi")
    private Integer jenisKoperasi;

    @Column(name = "email")
    private String email;

    @JsonIgnoreProperties({"koperasi", "hibernateLazyInitializer", "handler"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

    public Koperasi() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamaKoperasi() {
        return namaKoperasi;
    }

    public void setNamaKoperasi(String namaKoperasi) {
        this.namaKoperasi = namaKoperasi;
    }

    public String getAlamatKoperasi() {
        return alamatKoperasi;
    }

    public void setAlamatKoperasi(String alamatKoperasi) {
        this.alamatKoperasi = alamatKoperasi;
    }

    public String getTahunBerdiriKoperasi() {
        return tahunBerdiriKoperasi;
    }

    public void setTahunBerdiriKoperasi(String tahunBerdiriKoperasi) {
        this.tahunBerdiriKoperasi = tahunBerdiriKoperasi;
    }

    public String getNoIzinKoperasi() {
        return noIzinKoperasi;
    }

    public void setNoIzinKoperasi(String noIzinKoperasi) {
        this.noIzinKoperasi = noIzinKoperasi;
    }

    public String getNamaPendiri() {
        return namaPendiri;
    }

    public void setNamaPendiri(String namaPendiri) {
        this.namaPendiri = namaPendiri;
    }

    public String getLogoKoperasi() {
        return logoKoperasi;
    }

    public void setLogoKoperasi(String logoKoperasi) {
        this.logoKoperasi = logoKoperasi;
    }

    public Integer getJenisKoperasi() {
        return jenisKoperasi;
    }

    public void setJenisKoperasi(Integer jenisKoperasi) {
        this.jenisKoperasi = jenisKoperasi;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
