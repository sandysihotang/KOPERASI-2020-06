package io.github.sandy.service;

import io.github.sandy.model.*;
import io.github.sandy.repository.*;
import io.github.sandy.request.Requestbody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PeminjamanService {
    @Autowired
    PinjamanRepository pinjamanRepository;

    @Autowired
    PengaturanPinjamanRepository pengaturanPinjamanRepository;

    @Autowired
    AngotaKoperasiRepository angotaKoperasiRepository;

    @Autowired
    AngsuranRepository angsuranRepository;

    @Autowired
    KoperasiRepository koperasiRepository;

    @Autowired
    UserRepository userRepository;

    public void requestPinjaman(Map<String, Object> user, Requestbody requestbody) {
        Pinjaman pinjaman = new Pinjaman();
        PengaturanPinjaman pengaturanPinjaman = pengaturanPinjamanRepository.findFirstById(requestbody.getId()).get();

        User us = userRepository.getOne((Integer) user.get("id"));
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserIdInnerAnggota((Integer) user.get("id"));
        String kodePinjaman = getKodePinjaman((Integer) koperasi.get("id"));
        pinjaman.setPengaturanPinjaman(pengaturanPinjaman);
        pinjaman.setUser(us);
        pinjaman.setJaminan(requestbody.getJaminan());
        pinjaman.setStatus(5);
        pinjaman.setCreatedAt(new Date());
        pinjaman.setKoperasi(koperasiRepository.getOne((Integer) koperasi.get("id")));
        pinjaman.setKodePinjaman(kodePinjaman);
        pinjaman.setTenor(requestbody.getTenor());
        pinjaman.setJumlahPinjaman(Double.parseDouble(Integer.toString(requestbody.getPrice())) / 100);
        pinjamanRepository.save(pinjaman);
    }

    private String getKodePinjaman(Integer idKoperasi) {
        String kode = pinjamanRepository.getMaxKodePinjaman(idKoperasi);
        if (kode != null) {
            int noUrut = Integer.parseInt(kode.substring(1, 11));
            noUrut++;
            return String.format("L%010d", noUrut);
        }
        return "L0000000001";
    }

    public void actionfromPengurus(Requestbody requestbody, int id) {
        Pinjaman pinjaman = pinjamanRepository.getOne(id);
        if (pinjaman.getStatus() == requestbody.getStatus()) {
            return;
        }
        pinjaman.setTenor(requestbody.getTenor());
        pinjaman.setJaminan(requestbody.getJaminan());
        pinjaman.setJumlahPinjaman(Double.parseDouble(Integer.toString(requestbody.getPrice())) / 100);
        pinjaman.setStatus(requestbody.getStatus());
        pinjaman.setDatePengajuanDiterima(new Date());
        pinjamanRepository.save(pinjaman);
        if (requestbody.getStatus() == 2) {
            List<Angsuran> angsurans = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, pinjaman.getPengaturanPinjaman().getAmbangBatasDenda());
            for (int i = 0; i < pinjaman.getTenor(); ++i) {
                Angsuran angsuran = new Angsuran();
                angsuran.setPinjaman(pinjaman);
                angsuran.setAngsuranPokok(pinjaman.getJumlahPinjaman() / pinjaman.getTenor());
                angsuran.setBunga(pinjaman.getJumlahPinjaman() * (pinjaman.getPengaturanPinjaman().getBungaPinjaman() / 100));
                angsuran.setStatusBayar(false);
                double totalAngsuran = (pinjaman.getJumlahPinjaman() / pinjaman.getTenor()) + (pinjaman.getJumlahPinjaman() * (pinjaman.getPengaturanPinjaman().getBungaPinjaman() / 100));
                angsuran.setTotalAngsuran(totalAngsuran);
                angsuran.setTotalTagihan(totalAngsuran);
                angsuran.setUrutanKe(i + 1);
                calendar.add(Calendar.MONTH, 1);
                Date nextMonth = calendar.getTime();
                angsuran.setTanggalJatuhTempo(nextMonth);
                angsurans.add(angsuran);
            }
            angsuranRepository.saveAll(angsurans);
        }
    }
}
