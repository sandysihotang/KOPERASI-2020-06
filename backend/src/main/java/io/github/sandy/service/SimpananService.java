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

    public void saveAturanSimpanan(Requestbody requestbody, Integer koperasi) {
        if (pengaturanSimpananRepository.adaAturan(koperasi)) {
            pengaturanSimpananRepository.saveAturanSimpananUpdate(requestbody.getPokok(), koperasi, 1);
            pengaturanSimpananRepository.saveAturanSimpananUpdate(requestbody.getWajib(), koperasi, 2);
            pengaturanSimpananRepository.saveAturanSimpananUpdate(requestbody.getSukarela(), koperasi, 3);
        } else {
            pengaturanSimpananRepository.saveAturanSimpanan(requestbody.getPokok(), koperasi, 1);
            pengaturanSimpananRepository.saveAturanSimpanan(requestbody.getWajib(), koperasi, 2);
            pengaturanSimpananRepository.saveAturanSimpanan(requestbody.getSukarela(), koperasi, 3);
        }
    }

    public void saveActivasiSimpanan(Requestbody requestbody, Map<String, Object> koperasi, Integer user, Integer jenis_simpanan) {
//        Koperasi koperasi1 = koperasiRepository.getOne();
        AktivasiSimpanan aktivasiSimpanan = new AktivasiSimpanan();
        aktivasiSimpanan.setAktif(true);
        aktivasiSimpanan.setIdKoperasi((Integer) koperasi.get("id"));
        aktivasiSimpanan.setIdUser(user);
        aktivasiSimpanan.setJenisSimpanan(jenis_simpanan);
        aktivasiSimpanan.setTotalSimpanan(requestbody.getJumlahSimpanan());
        aktivasiSimpanan.setTanggalMulai(new Date(requestbody.getDate()));
        aktivasiSimpanan.setCreatedAt(new Date());
        aktivasiSimpananRepository.save(aktivasiSimpanan);

//        TransaksiSimpanan transaksiSimpanan = new TransaksiSimpanan();
//        transaksiSimpanan.setAktivasiSimpanan(aktivasiSimpanan);
//        transaksiSimpanan.setCreatedAt(new Date());
//        transaksiSimpanan.setJumlahTransaksi(requestbody.getJumlahSimpanan().intValue());
//        transaksiSimpanan.setJenisTransaksi(1);
//        transaksiSimpanan.setKodeTransaksi();

        transaksiSimpananRepository.insertToTransaksi(requestbody.getJumlahSimpanan().intValue(), aktivasiSimpanan.getId(), new Date(),
                1, getKodeTransaksiSimpanan((Integer) koperasi.get("id")));

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
        Map<String, Object> koperasi = koperasiRepository.getKoperasibySimpanan(id);
        TransaksiSimpanan transaksiSimpanan = new TransaksiSimpanan();
        transaksiSimpanan.setKodeTransaksi(getKodeTransaksiSimpanan((Integer) koperasi.get("id")));
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
        transaksiSimpanan.setKodeTransaksi(getKodeTransaksiSimpanan(aktivasiSimpanan.getIdKoperasi()));
        transaksiSimpanan.setJumlahTransaksi(requestbody.getJumlahTransaksi());
        transaksiSimpanan.setJenisTransaksi(2);
        transaksiSimpanan.setCreatedAt(new Date(requestbody.getDate()));
        transaksiSimpanan.setAktivasiSimpanan(aktivasiSimpanan);
        transaksiSimpananRepository.save(transaksiSimpanan);

        aktivasiSimpanan.setTotalSimpanan(aktivasiSimpanan.getTotalSimpanan() - requestbody.getJumlahTransaksi());
        aktivasiSimpananRepository.save(aktivasiSimpanan);
    }
}
