package io.github.sandy.request;

import org.springframework.web.multipart.MultipartFile;

public class Requestbody {
    private int id;
    private String kategori;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String telepon;
    private String alamat;
    private String date;
    private String name;
    private String pendiri;
    private String izin;
    private String jenis;
    private MultipartFile image;
    private boolean state;
    private int status;
    private String formField;
    private String fieldData;
    private Double bungaPinjaman;
    private Integer minTenor;
    private Integer maxTenor;
    private Integer ambangBatasDenda;
    private Double persentaseDenda;
    private String jaminan;
    private Integer price;
    private Integer tenor;
    private Integer pokok;
    private Integer wajib;
    private Integer sukarela;
    private Long jumlahSimpanan;
    private Integer jumlahTransaksi;
    private String barCode;
    private String namaProduk;
    private Integer hargaBeli;
    private Integer hargaJualAnggota;
    private Integer hargaJualNonAnggota;
    private Integer kategoriProduk;
    private Integer jumlahBarang;
    private Boolean anggota;
    private Integer jumlahBeli;
    private Integer uangBeli;

    public Integer getUangBeli() {
        return uangBeli;
    }

    public void setUangBeli(Integer uangBeli) {
        this.uangBeli = uangBeli;
    }

    public Integer getJumlahBeli() {
        return jumlahBeli;
    }

    public void setJumlahBeli(Integer jumlahBeli) {
        this.jumlahBeli = jumlahBeli;
    }

    public Boolean getAnggota() {
        return anggota;
    }

    public void setAnggota(Boolean anggota) {
        this.anggota = anggota;
    }

    public String getBarCode() {
        return barCode;
    }

    public Integer getJumlahBarang() {
        return jumlahBarang;
    }

    public void setJumlahBarang(Integer jumlahBarang) {
        this.jumlahBarang = jumlahBarang;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public Integer getHargaBeli() {
        return hargaBeli;
    }

    public void setHargaBeli(Integer hargaBeli) {
        this.hargaBeli = hargaBeli;
    }

    public Integer getHargaJualAnggota() {
        return hargaJualAnggota;
    }

    public void setHargaJualAnggota(Integer hargaJualAnggota) {
        this.hargaJualAnggota = hargaJualAnggota;
    }

    public Integer getHargaJualNonAnggota() {
        return hargaJualNonAnggota;
    }

    public void setHargaJualNonAnggota(Integer hargaJualNonAnggota) {
        this.hargaJualNonAnggota = hargaJualNonAnggota;
    }

    public Integer getKategoriProduk() {
        return kategoriProduk;
    }

    public void setKategoriProduk(Integer kategoriProduk) {
        this.kategoriProduk = kategoriProduk;
    }

    public Integer getJumlahTransaksi() {
        return jumlahTransaksi;
    }

    public void setJumlahTransaksi(Integer jumlahTransaksi) {
        this.jumlahTransaksi = jumlahTransaksi;
    }

    public Long getJumlahSimpanan() {
        return jumlahSimpanan;
    }

    public void setJumlahSimpanan(Long jumlahSimpanan) {
        this.jumlahSimpanan = jumlahSimpanan;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getJaminan() {
        return jaminan;
    }

    public Integer getPokok() {
        return pokok;
    }

    public void setPokok(Integer pokok) {
        this.pokok = pokok;
    }

    public Integer getWajib() {
        return wajib;
    }

    public void setWajib(Integer wajib) {
        this.wajib = wajib;
    }

    public Integer getSukarela() {
        return sukarela;
    }

    public void setSukarela(Integer sukarela) {
        this.sukarela = sukarela;
    }

    public void setJaminan(String jaminan) {
        this.jaminan = jaminan;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getFormField() {
        return formField;
    }

    public String getFieldData() {
        return fieldData;
    }

    public Double getBungaPinjaman() {
        return bungaPinjaman;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getTenor() {
        return tenor;
    }

    public void setTenor(Integer tenor) {
        this.tenor = tenor;
    }

    public void setBungaPinjaman(Double bungaPinjaman) {
        this.bungaPinjaman = bungaPinjaman;
    }

    public Integer getMinTenor() {
        return minTenor;
    }

    public void setMinTenor(Integer minTenor) {
        this.minTenor = minTenor;
    }

    public Integer getMaxTenor() {
        return maxTenor;
    }

    public void setMaxTenor(Integer maxTenor) {
        this.maxTenor = maxTenor;
    }

    public Integer getAmbangBatasDenda() {
        return ambangBatasDenda;
    }

    public void setAmbangBatasDenda(Integer ambangBatasDenda) {
        this.ambangBatasDenda = ambangBatasDenda;
    }

    public Double getPersentaseDenda() {
        return persentaseDenda;
    }

    public void setPersentaseDenda(Double persentaseDenda) {
        this.persentaseDenda = persentaseDenda;
    }

    public void setFieldData(String fieldData) {
        this.fieldData = fieldData;
    }

    public void setFormField(String formField) {
        this.formField = formField;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Requestbody() {
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPendiri() {
        return pendiri;
    }

    public void setPendiri(String pendiri) {
        this.pendiri = pendiri;
    }

    public String getIzin() {
        return izin;
    }

    public void setIzin(String izin) {
        this.izin = izin;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
