package io.github.sandy.service;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.config.AuthorizationServerConfiguration;
import io.github.sandy.model.*;
import io.github.sandy.repository.*;
import io.github.sandy.request.Requestbody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class KoperasiService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleUserRepository roleUserRepository;

    @Autowired
    DetailUserRepository detailUserRepository;

    @Autowired
    AngsuranRepository angsuranRepository;

    @Autowired
    AuthorizationServerConfiguration auth;

    @Autowired
    KoperasiRepository koperasiRepository;

    @Autowired
    DaftarAnggotaKoperasiRepository daftarAnggotaKoperasiRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    AngotaKoperasiRepository angotaKoperasiRepository;

    @Autowired
    KoperasiPengaturanPinjamanRepository koperasiPengaturanPinjamanRepository;

    @Autowired
    PengaturanPinjamanRepository pengaturanPinjamanRepository;

    @Autowired
    PinjamanRepository pinjamanRepository;

    @Autowired
    TransaksiSimpananRepository transaksiSimpananRepository;

    public Err createKoperasi(Requestbody requestbody, String uname) throws Exception {
        User user = userRepository.findByUsername(uname).get();

        Koperasi koperasi = new Koperasi();

        koperasi.setAlamatKoperasi(requestbody.getAlamat());
        koperasi.setNamaKoperasi(requestbody.getName());
        koperasi.setJenisKoperasi(requestbody.getJenis().trim().equals("Koperasi Serba Usaha (KSU)") ? 1 : 2);
        koperasi.setNamaPendiri(requestbody.getPendiri());
        koperasi.setNoIzinKoperasi(requestbody.getIzin());
        koperasi.setTahunBerdiriKoperasi(new Date(requestbody.getDate()));
        koperasi.setEmail(requestbody.getEmail());
        koperasi.setUser(user);
        String path = saveImage(requestbody.getImage());
        koperasi.setLogoKoperasi(path);
        koperasiRepository.save(koperasi);

        User user1 = userRepository.getOne(user.getId());
        user1.setHaveKoperasi(2);
        userRepository.save(user1);

        return new Err(200, "Koperasi berhasil");
    }

    private String saveImage(MultipartFile image) {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
        String name = myDateObj.format(myFormatObj);

        File curFile = new File("");
        String helper = curFile.getAbsolutePath();
        String curDir = helper + "/backend/src/main/resources/static/images/";
        String pict = name + ".png";
        Path path = Paths.get(curDir + pict);
        byte[] images = new byte[0];
        try {
            images = image.getBytes();
            Files.write(path, images);
            return "/backend/src/main/resources/static/images/" + pict;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void changeStateKoperasi(int id, boolean state) {
        Koperasi koperasi = koperasiRepository.findById(id).get();
        MailSender mailSender = new MailSender();
//        mailSender.sendEmailSetStateKoperasi(javaMailSender, koperasi.getEmail(), (state ? "Koperasi Telah Diaktifkan" : "Maaf Koperasi dinonaktifkan"));

        User user = userRepository.getOne(koperasi.getUser().getId());
        user.setHaveKoperasi((!state ? 3 : 2));
        userRepository.save(user);

    }

    public void saveFormRegisterMember(String uname, String pattern) {
        User user = userRepository.findByUsername(uname).get();
        Koperasi koperasi = user.getKoperasi();


        FieldDaftarAnggota fieldDaftarAnggota = new FieldDaftarAnggota();
        fieldDaftarAnggota.setKoperasi(koperasi);
        fieldDaftarAnggota.setPatternField(pattern);

        daftarAnggotaKoperasiRepository.save(fieldDaftarAnggota);

        Koperasi changeStateForm = koperasiRepository.getOne(koperasi.getId());
        koperasi.setHaveFieldRegisterMember(true);
        koperasiRepository.save(changeStateForm);
    }

    public void saveUser(Requestbody requestbody, Koperasi koperasi) {
        User user = new User();
        user.setUsername(requestbody.getUsername());
        user.setPassword(auth.passwordEncoder.encode(requestbody.getPassword()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setEmail(requestbody.getEmail());
        userRepository.save(user);

        Role role = roleRepository.findFirstByName("ROLE_anggota");
        RoleUser roleUser = new RoleUser(role.getId(), user.getId());
        roleUserRepository.save(roleUser);

        UserDetail userDetail = new UserDetail();
        userDetail.setFirstName(requestbody.getFirstName());
        userDetail.setLastName(requestbody.getLastName());
        userDetail.setUser(user);
        userDetail.setAddress(requestbody.getAlamat());
        userDetail.setNoTelepon(requestbody.getTelepon());
        detailUserRepository.save(userDetail);

        AnggotaKoperasi anggotaKoperasi = new AnggotaKoperasi();
        anggotaKoperasi.setData(requestbody.getFieldData());
        anggotaKoperasi.setKoperasi(koperasi);
        anggotaKoperasi.setUser(user);

        angotaKoperasiRepository.save(anggotaKoperasi);
    }

    public void savePengaturanPeminjaman(Koperasi koperasi, Requestbody requestbody) {
        PengaturanPinjaman pengaturanPinjaman = new PengaturanPinjaman();
        pengaturanPinjaman.setBungaPinjaman(requestbody.getBungaPinjaman() / 100);
        pengaturanPinjaman.setAmbangBatasDenda(requestbody.getAmbangBatasDenda());
        pengaturanPinjaman.setMaxTenor(requestbody.getMaxTenor());
        pengaturanPinjaman.setMinTenor(requestbody.getMinTenor());
        pengaturanPinjaman.setPersentaseDenda(requestbody.getPersentaseDenda() / 100);

        pengaturanPinjamanRepository.save(pengaturanPinjaman);
        if (koperasiPengaturanPinjamanRepository.existsByKoperasiAndStatus(koperasi, true)) {
            KoperasiPengaturanPinjaman koperasiPengaturanPinjaman = koperasiPengaturanPinjamanRepository.getFirstByKoperasiAndStatus(koperasi, true).get();
            KoperasiPengaturanPinjaman update = koperasiPengaturanPinjamanRepository.getOne(koperasiPengaturanPinjaman.getId());
            update.setStatus(false);
            koperasiPengaturanPinjamanRepository.save(update);
        }
        KoperasiPengaturanPinjaman koperasiPengaturanPinjaman = new KoperasiPengaturanPinjaman();
        koperasiPengaturanPinjaman.setStatus(true);
        koperasiPengaturanPinjaman.setKoperasi(koperasi);
        koperasiPengaturanPinjaman.setPengaturanPinjaman(pengaturanPinjaman);
        koperasiPengaturanPinjamanRepository.save(koperasiPengaturanPinjaman);
    }

    public void checkDenda() {
        List<Angsuran> angsurans = angsuranRepository.getExistDenda();
        for (Angsuran angsuran : angsurans) {
            LocalDate l = LocalDate.of(angsuran.getTanggalJatuhTempo().getYear(), angsuran.getTanggalJatuhTempo().getMonth(), angsuran.getTanggalJatuhTempo().getDay());
            LocalDate now = LocalDate.of(new Date().getYear(), new Date().getMonth(), new Date().getDay());
            Long days = ChronoUnit.DAYS.between(l, now);
            Pinjaman pinjaman = pinjamanRepository.getFirstById(angsuran.getPinjaman().getId());
            PengaturanPinjaman pengaturanPinjaman = pengaturanPinjamanRepository.getFirstById(pinjaman.getPengaturanPinjaman().getId());
            long day = days + pengaturanPinjaman.getAmbangBatasDenda();
            Double total = (pinjaman.getJumlahPinjaman() * pengaturanPinjaman.getPersentaseDenda() / 100) * day;
            angsuran.setDenda(total);
            angsuranRepository.save(angsuran);
        }
    }

    public Map<String, Object> getLaporanPemasukanDanLaba(Koperasi koperasi, Date dateFrom, Date dateTo) {
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.getNamaKoperasi());
        Long realisasiJasa = angsuranRepository.getRealisasiJasaDateFromTo(koperasi.getId(), dateFrom, dateTo);
        data.put("realisasiJasa", realisasiJasa);
        Long denda = angsuranRepository.getDenda(koperasi.getId(), dateFrom, dateTo);
        data.put("tunggakan", denda);
        Long simpananPokok = transaksiSimpananRepository.getTransaksi(koperasi.getId(), 1, 1, dateFrom, dateTo);
        data.put("simpananPokok", simpananPokok);
        Long simpananWajib = transaksiSimpananRepository.getTransaksi(koperasi.getId(), 1, 2, dateFrom, dateTo);
        data.put("simpananWajib", simpananWajib);
        Long simpananSukarela = transaksiSimpananRepository.getTransaksi(koperasi.getId(), 1, 3, dateFrom, dateTo);
        data.put("simpananSukarela", simpananSukarela);
        return data;
    }

    public Map<String, Object> getLaporanTransaksiSimpanan(Koperasi koperasi, Date dateFrom, Date dateTo) {
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.getNamaKoperasi());
        List<TransaksiSimpanan> transaksiSimpanans = transaksiSimpananRepository.getTransaksiSimpananLaporan(koperasi.getId(), dateFrom, dateTo);
        List<Map<String, Object>> res = new ArrayList<>();
        for (TransaksiSimpanan transaksiSimpanan : transaksiSimpanans) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("noTransaksi", transaksiSimpanan.getKodeTransaksi());
            temp.put("nama", transaksiSimpanan.getAktivasiSimpanan().getUser().getUserDetail().getFirstName() + " " + transaksiSimpanan.getAktivasiSimpanan().getUser().getUserDetail().getLastName());
            temp.put("tipeTransaksi", (transaksiSimpanan.getJenisTransaksi() == 1 ? "Setor Dana" : "Tarik Dana"));
            temp.put("produk", (transaksiSimpanan.getAktivasiSimpanan().getJenisSimpanan() == 1 ? "Simpanan Pokok" : (transaksiSimpanan.getAktivasiSimpanan().getJenisSimpanan() == 2 ? "Simpanan Wajib" : "Simpanan Sukarela")));
            temp.put("tglTransaksi", transaksiSimpanan.getCreatedAt());
            temp.put("nominal", transaksiSimpanan.getJumlahTransaksi());
            res.add(temp);
        }
        data.put("dataTable", res);
        return data;
    }

    public Map<String, Object> getLaporanTransaksiPinjaman(Koperasi koperasi, Date dateFrom, Date dateTo) {
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.getNamaKoperasi());
        List<Pinjaman> transaksiPinjaman = pinjamanRepository.getLaporanPinjaman(koperasi.getId(), dateFrom, dateTo);
        List<Map<String, Object>> res = new ArrayList<>();
        double totPinjaman = 0.0, totLaba = 0.0;
        for (Pinjaman pinjaman : transaksiPinjaman) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("noTransaksi", pinjaman.getKodePinjaman());
            temp.put("nama", pinjaman.getUser().getUserDetail().getFirstName() + " " + pinjaman.getUser().getUserDetail().getLastName());
            temp.put("jaminan", pinjaman.getJaminan());
            temp.put("jumlahPinjaman", pinjaman.getJumlahPinjaman());
            temp.put("tenor", pinjaman.getTenor());
            totPinjaman += pinjaman.getJumlahPinjaman();
            double total = 0.0;
            for (Angsuran angsuran : pinjaman.getAngsuran()) {
                total += angsuran.getBunga() + angsuran.getDenda();
            }
            totLaba += total;
            temp.put("laba", total);
            temp.put("tglPengajuan", pinjaman.getCreatedAt());
            temp.put("tglDiterimaPengajuan", pinjaman.getDatePengajuanDiterima());
            temp.put("tglSelesaiCicilan", (pinjaman.getUpdatedAt() == null ? "-" : pinjaman.getUpdatedAt()));
            res.add(temp);
        }
        data.put("totLaba", totLaba);
        data.put("totPinjaman", totPinjaman);
        data.put("dataTable", res);
        return data;
    }
}
