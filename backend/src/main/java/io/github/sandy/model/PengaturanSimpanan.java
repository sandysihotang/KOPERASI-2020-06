package io.github.sandy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "aturan_simpanan")
public class PengaturanSimpanan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "minimal_simpanan")
    @NotNull
    private Integer minimalSimpanan;

    @Column(name = "jenis_simpanan")
    @NotNull
    private Integer jenisSimpanan;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_koperasi", nullable = false)
    private Koperasi koperasi;

    public PengaturanSimpanan() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMinimalSimpanan() {
        return minimalSimpanan;
    }

    public void setMinimalSimpanan(Integer minimalSimpanan) {
        this.minimalSimpanan = minimalSimpanan;
    }

    public Integer getJenisSimpanan() {
        return jenisSimpanan;
    }

    public void setJenisSimpanan(Integer jenisSimpanan) {
        this.jenisSimpanan = jenisSimpanan;
    }

    public Koperasi getKoperasi() {
        return koperasi;
    }

    public void setKoperasi(Koperasi koperasi) {
        this.koperasi = koperasi;
    }
}
