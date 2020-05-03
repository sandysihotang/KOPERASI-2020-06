package io.github.sandy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Date tahunBerdiriKoperasi;

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

    @Column(name = "have_field_register_member")
    private boolean haveFieldRegisterMember;

    @JsonManagedReference
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "koperasi")
    private Set<AnggotaKoperasi> anggotaKoperasis = new HashSet<>();

    @JsonManagedReference
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "koperasi")
    private Set<AktivasiSimpanan> aktivasiSimpanan = new HashSet<>();

    @JsonManagedReference
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "koperasi")
    private Set<Pinjaman> pinjaman = new HashSet<>();

    @JsonManagedReference
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "koperasi")
    private Set<KoperasiPengaturanPinjaman> koperasiPengaturanPinjaman = new HashSet<>();

    @JsonManagedReference
    @Fetch(FetchMode.JOIN)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "koperasi")
    private Set<PengaturanSimpanan> pengaturanSimpanan = new HashSet<>();

    @JsonIgnoreProperties({"koperasi", "hibernateLazyInitializer", "handler"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

    @JsonIgnoreProperties({"koperasi", "hibernateLazyInitializer", "handler"})
    @OneToOne(mappedBy = "koperasi", fetch = FetchType.LAZY)
    private FieldDaftarAnggota fieldDaftarAnggota;

    public Set<AktivasiSimpanan> getAktivasiSimpanan() {
        return aktivasiSimpanan;
    }

    public void setAktivasiSimpanan(Set<AktivasiSimpanan> aktivasiSimpanan) {
        this.aktivasiSimpanan = aktivasiSimpanan;
    }

    public Koperasi() {
    }

    public Set<PengaturanSimpanan> getPengaturanSimpanan() {
        return pengaturanSimpanan;
    }

    public void setPengaturanSimpanan(Set<PengaturanSimpanan> pengaturanSimpanan) {
        this.pengaturanSimpanan = pengaturanSimpanan;
    }

    public Set<AnggotaKoperasi> getAnggotaKoperasis() {
        return anggotaKoperasis;
    }

    public void setAnggotaKoperasis(Set<AnggotaKoperasi> anggotaKoperasis) {
        this.anggotaKoperasis = anggotaKoperasis;
    }

    public Set<Pinjaman> getPinjaman() {
        return pinjaman;
    }

    public void setPinjaman(Set<Pinjaman> pinjaman) {
        this.pinjaman = pinjaman;
    }

    public Set<KoperasiPengaturanPinjaman> getKoperasiPengaturanPinjaman() {
        return koperasiPengaturanPinjaman;
    }

    public void setKoperasiPengaturanPinjaman(Set<KoperasiPengaturanPinjaman> koperasiPengaturanPinjaman) {
        this.koperasiPengaturanPinjaman = koperasiPengaturanPinjaman;
    }

    public boolean isHaveFieldRegisterMember() {
        return haveFieldRegisterMember;
    }

    public void setHaveFieldRegisterMember(boolean haveFieldRegisterMember) {
        this.haveFieldRegisterMember = haveFieldRegisterMember;
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

    public Date getTahunBerdiriKoperasi() {
        return tahunBerdiriKoperasi;
    }

    public void setTahunBerdiriKoperasi(Date tahunBerdiriKoperasi) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FieldDaftarAnggota getFieldDaftarAnggota() {
        return fieldDaftarAnggota;
    }

    public void setFieldDaftarAnggota(FieldDaftarAnggota fieldDaftarAnggota) {
        this.fieldDaftarAnggota = fieldDaftarAnggota;
    }
}
