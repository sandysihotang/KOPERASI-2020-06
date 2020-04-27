package io.github.sandy.service;

import io.github.sandy.model.PengaturanPinjaman;
import io.github.sandy.model.Pinjaman;
import io.github.sandy.model.User;
import io.github.sandy.repository.PengaturanPinjamanRepository;
import io.github.sandy.repository.PinjamanRepository;
import io.github.sandy.request.Requestbody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PeminjamanService {
    @Autowired
    PinjamanRepository pinjamanRepository;

    @Autowired
    PengaturanPinjamanRepository pengaturanPinjamanRepository;
    public void requestPinjaman(User user, Requestbody requestbody) {
        Pinjaman pinjaman = new Pinjaman();
        PengaturanPinjaman pengaturanPinjaman = pengaturanPinjamanRepository.findFirstById(requestbody.getId()).get();

        pinjaman.setPengaturanPinjaman(pengaturanPinjaman);
        pinjaman.setUser(user);
        pinjaman.setJaminan(requestbody.getJaminan());
//        pinjaman
    }
}
