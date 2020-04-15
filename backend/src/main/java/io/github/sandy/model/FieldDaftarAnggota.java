package io.github.sandy.model;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.sandy.config.StringMapConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "field_daftar_anggota")
public class FieldDaftarAnggota implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pattern_field", columnDefinition = "JSON")
    @Convert(converter = StringMapConverter.class)
    private Map<String, String> patternField;

    @JsonIgnoreProperties({"field_daftar_anggota", "hibernateLazyInitializer", "handler"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "koperasi_id", referencedColumnName = "id")
    private Koperasi koperasi;

    public FieldDaftarAnggota() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Map<String, String> getPatternField() {
        return patternField;
    }

    public void setPatternField(Map<String, String> patternField) {
        this.patternField = patternField;
    }

    public Koperasi getKoperasi() {
        return koperasi;
    }

    public void setKoperasi(Koperasi koperasi) {
        this.koperasi = koperasi;
    }
}
