package io.github.sandy.service;

import io.github.sandy.model.*;
import io.github.sandy.repository.AktivasiSimpananRepository;
import io.github.sandy.repository.KoperasiRepository;
import io.github.sandy.repository.PengaturanSimpananRepository;
import io.github.sandy.repository.TransaksiSimpananRepository;
import io.github.sandy.request.Requestbody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SimpananService {
    @Autowired
    PengaturanSimpananRepository pengaturanSimpananRepository;

    @Autowired
    AktivasiSimpananRepository aktivasiSimpananRepository;

    @Autowired
    TransaksiSimpananRepository transaksiSimpananRepository;

    @Autowired
    KoperasiRepository koperasiRepository;

    public void saveAturanSimpanan(Requestbody requestbody, Koperasi koperasi) {
        List<PengaturanSimpanan> pengaturanSimpananList = new ArrayList<>();
        PengaturanSimpanan pengaturanSimpanan = new PengaturanSimpanan();
        pengaturanSimpanan.setJenisSimpanan(1);
        pengaturanSimpanan.setKoperasi(koperasi);
        pengaturanSimpanan.setMinimalSimpanan(requestbody.getPokok());
        pengaturanSimpananList.add(pengaturanSimpanan);
        pengaturanSimpanan = new PengaturanSimpanan();
        pengaturanSimpanan.setJenisSimpanan(2);
        pengaturanSimpanan.setKoperasi(koperasi);
        pengaturanSimpanan.setMinimalSimpanan(requestbody.getWajib());
        pengaturanSimpananList.add(pengaturanSimpanan);
        pengaturanSimpanan = new PengaturanSimpanan();
        pengaturanSimpanan.setJenisSimpanan(3);
        pengaturanSimpanan.setKoperasi(koperasi);
        pengaturanSimpanan.setMinimalSimpanan(requestbody.getSukarela());
        pengaturanSimpananList.add(pengaturanSimpanan);

        pengaturanSimpananRepository.saveAll(pengaturanSimpananList);

    }

    public void saveActivasiSimpanan(Requestbody requestbody, Map<String,Object> koperasi, User user, Integer jenis_simpanan) {
        Koperasi koperasi1 = koperasiRepository.getOne((Integer) koperasi.get("id"));
        AktivasiSimpanan aktivasiSimpanan = new AktivasiSimpanan();
        aktivasiSimpanan.setAktif(true);
        aktivasiSimpanan.setKoperasi(koperasi1);
        aktivasiSimpanan.setUser(user);
        aktivasiSimpanan.setJenisSimpanan(jenis_simpanan);
        aktivasiSimpanan.setTotalSimpanan(requestbody.getJumlahSimpanan());
        aktivasiSimpanan.setTanggalMulai(new Date(requestbody.getDate()));
        aktivasiSimpanan.setCreatedAt(new Date());
        aktivasiSimpananRepository.save(aktivasiSimpanan);

        TransaksiSimpanan transaksiSimpanan = new TransaksiSimpanan();
        transaksiSimpanan.setAktivasiSimpanan(aktivasiSimpanan);
        transaksiSimpanan.setCreatedAt(new Date());
        transaksiSimpanan.setJumlahTransaksi(requestbody.getJumlahSimpanan().intValue());
        transaksiSimpanan.setJenisTransaksi(1);
        transaksiSimpanan.setKodeTransaksi(getKodeTransaksiSimpanan(koperasi1.getId()));

        transaksiSimpananRepository.save(transaksiSimpanan);

    }

    private String getKodeTransaksiSimpanan(Integer idKoperasi) {
        String kode = transaksiSimpananRepository.getMaxKodetransaksiSimpanan(idKoperasi);
        if (kode != null) {
            int noUrut = Integer.parseInt(kode.substring(1, 11));
            noUrut++;
            return String.format("S%010d", noUrut);
        }
        return "S0000000001";
    }

    public void saveTransaksiSimpanan(Integer id, Requestbody requestbody) {
        AktivasiSimpanan aktivasiSimpanan = aktivasiSimpananRepository.getOne(id);
        TransaksiSimpanan transaksiSimpanan = new TransaksiSimpanan();
        transaksiSimpanan.setKodeTransaksi(getKodeTransaksiSimpanan(aktivasiSimpanan.getKoperasi().getId()));
        transaksiSimpanan.setJumlahTransaksi(requestbody.getJumlahTransaksi());
        transaksiSimpanan.setJenisTransaksi(1);
        transaksiSimpanan.setCreatedAt(new Date(requestbody.getDate()));
        transaksiSimpanan.setAktivasiSimpanan(aktivasiSimpanan);
        transaksiSimpananRepository.save(transaksiSimpanan);

        aktivasiSimpanan.setTotalSimpanan(aktivasiSimpanan.getTotalSimpanan() + requestbody.getJumlahTransaksi());
        aktivasiSimpananRepository.save(aktivasiSimpanan);
    }

    public void saveTransaksiPenarikanSimpanan(Integer id, Requestbody requestbody) {
        AktivasiSimpanan aktivasiSimpanan = aktivasiSimpananRepository.getOne(id);
        TransaksiSimpanan transaksiSimpanan = new TransaksiSimpanan();
        transaksiSimpanan.setKodeTransaksi(getKodeTransaksiSimpanan(aktivasiSimpanan.getKoperasi().getId()));
        transaksiSimpanan.setJumlahTransaksi(requestbody.getJumlahTransaksi());
        transaksiSimpanan.setJenisTransaksi(2);
        transaksiSimpanan.setCreatedAt(new Date(requestbody.getDate()));
        transaksiSimpanan.setAktivasiSimpanan(aktivasiSimpanan);
        transaksiSimpananRepository.save(transaksiSimpanan);

        aktivasiSimpanan.setTotalSimpanan(aktivasiSimpanan.getTotalSimpanan() - requestbody.getJumlahTransaksi());
        aktivasiSimpananRepository.save(aktivasiSimpanan);
    }
}
