package io.github.sandy.service;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.model.Koperasi;
import io.github.sandy.model.User;
import io.github.sandy.repository.KoperasiRepository;
import io.github.sandy.repository.UserRepository;
import io.github.sandy.request.Requestbody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KoperasiService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    KoperasiRepository koperasiRepository;

    public Err createKoperasi(Requestbody requestbody, String uname) {
        User user = userRepository.findByUsername(uname).get();

        Koperasi koperasi = new Koperasi();

        koperasi.setAlamatKoperasi(requestbody.getAlamat());
        koperasi.setNamaKoperasi(requestbody.getName());
        koperasi.setJenisKoperasi(requestbody.getJenis().trim().equals("Koperasi Serba Usaha (KSU)") ? 1 : 2);
        koperasi.setNamaPendiri(requestbody.getPendiri());
        koperasi.setNoIzinKoperasi(requestbody.getIzin());
        koperasi.setTahunBerdiriKoperasi(requestbody.getDate());
        koperasi.setEmail(requestbody.getEmail());
        koperasi.setUser(user);

        koperasiRepository.save(koperasi);

        User user1 = userRepository.getOne(user.getId());
        user1.setHaveKoperasi(2);
        userRepository.save(user1);

        return new Err(200,"Koperasi berhasil");
    }
}
