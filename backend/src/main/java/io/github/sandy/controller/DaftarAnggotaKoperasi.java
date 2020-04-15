package io.github.sandy.controller;

import io.github.sandy.model.FieldDaftarAnggota;
import io.github.sandy.repository.DaftarAnggotaKoperasiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DaftarAnggotaKoperasi {
    @Autowired
    DaftarAnggotaKoperasiRepository daftarAnggotaKoperasiRepository;

    @RequestMapping(value = "/ss", method = RequestMethod.GET)
    public List<FieldDaftarAnggota> get(){
        return daftarAnggotaKoperasiRepository.findAll();
    }

}
