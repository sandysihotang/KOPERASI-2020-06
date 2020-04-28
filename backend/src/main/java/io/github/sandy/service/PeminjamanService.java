package io.github.sandy.service;

import io.github.sandy.model.Koperasi;
import io.github.sandy.model.PengaturanPinjaman;
import io.github.sandy.model.Pinjaman;
import io.github.sandy.model.User;
import io.github.sandy.repository.AngotaKoperasiRepository;
import io.github.sandy.repository.KoperasiRepository;
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

    @Autowired
    AngotaKoperasiRepository angotaKoperasiRepository;

    public void requestPinjaman(User user, Requestbody requestbody) {
        Pinjaman pinjaman = new Pinjaman();
        PengaturanPinjaman pengaturanPinjaman = pengaturanPinjamanRepository.findFirstById(requestbody.getId()).get();

        String kodePinjaman = getKodePinjaman();
        Koperasi koperasi = angotaKoperasiRepository.findFirstByUser(user).get().getKoperasi();
        pinjaman.setPengaturanPinjaman(pengaturanPinjaman);
        pinjaman.setUser(user);
        pinjaman.setJaminan(requestbody.getJaminan());
        pinjaman.setStatus(5);
        pinjaman.setKoperasi(koperasi);
        pinjaman.setKodePinjaman(kodePinjaman);
        pinjaman.setTenor(requestbody.getTenor());
        pinjaman.setJumlahPinjaman(Double.parseDouble(Integer.toString(requestbody.getPrice())) / 100);
        pinjamanRepository.save(pinjaman);
    }

    private String getKodePinjaman() {
        String kode = pinjamanRepository.getMaxKodePinjaman();
        if (kode != null) {
            int noUrut = Integer.parseInt(kode.substring(1, 11));
            noUrut++;
            return String.format("L%010d", noUrut);
        }
        return "L0000000001";
    }
}
