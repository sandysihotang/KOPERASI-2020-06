package io.github.sandy.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class Requestbody {
    private int id;
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
    private String formField;
    private String fieldData;
    private Double bungaPinjaman;
    private Integer minTenor;
    private Integer maxTenor;
    private Integer ambangBatasDenda;
    private Double persentaseDenda;

    public String getFormField() {
        return formField;
    }

    public String getFieldData() {
        return fieldData;
    }

    public Double getBungaPinjaman() {
        return bungaPinjaman;
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
