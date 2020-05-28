package io.github.sandy.service;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.config.AuthorizationServerConfiguration;
import io.github.sandy.gdrive.DriveQuickstart;
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

    @Autowired
    ProdukBaruRepository produkBaruRepository;

    @Autowired
    PenjualanProdukRepository penjualanProdukRepository;

    @Autowired
    NotifikasiAnggotaRepository notifikasiAnggotaRepository;

    @Autowired
    LaporanKoperasiRepository laporanKoperasiRepository;


    public Err createKoperasi(Requestbody requestbody, String uname) throws Exception {
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Koperasi koperasi = new Koperasi();

        koperasi.setAlamatKoperasi(requestbody.getAlamat());
        koperasi.setNamaKoperasi(requestbody.getName());
        koperasi.setJenisKoperasi(requestbody.getJenis().trim().equals("Koperasi Serba Usaha (KSU)") ? 1 : 2);
        koperasi.setNamaPendiri(requestbody.getPendiri());
        koperasi.setNoIzinKoperasi(requestbody.getIzin());
        koperasi.setTahunBerdiriKoperasi(new Date(requestbody.getDate()));
        koperasi.setEmail(requestbody.getEmail());
        koperasi.setUser(userRepository.getOne((Integer) user.get("id")));
        String path = saveImage(requestbody.getImage());
        if (!path.isEmpty()) {
            DriveQuickstart driveQuickstart = new DriveQuickstart();
            File t = new File("");
            File file = new File(t.getAbsolutePath() + path);
            path = driveQuickstart.uploadLogo(file);
            file.delete();
        }
        koperasi.setLogoKoperasi(path);
        koperasiRepository.save(koperasi);

        User user1 = userRepository.getOne((Integer) user.get("id"));
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

    public void changeStateKoperasi(int id, String text, boolean state) {
        Map<String, Object> koperasi = koperasiRepository.getKoperasiID(id);
        MailSender mailSender = new MailSender();
        mailSender.sendEmailSetStateKoperasi(javaMailSender, (String) koperasi.get("email"), text, (state ? "Koperasi Telah Diaktifkan" : "Maaf Koperasi dinonaktifkan"));

        userRepository.update((Integer) koperasi.get("id_user"), (!state ? 3 : 2));
    }

    public void saveFormRegisterMember(Map<String, Object> user, String pattern) {
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));

        FieldDaftarAnggota fieldDaftarAnggota = new FieldDaftarAnggota();
        fieldDaftarAnggota.setKoperasi(koperasiRepository.getOne((Integer) koperasi.get("id")));
        fieldDaftarAnggota.setPatternField(pattern);

        daftarAnggotaKoperasiRepository.save(fieldDaftarAnggota);

        Koperasi changeStateForm = koperasiRepository.getOne((Integer) koperasi.get("id"));
        koperasiRepository.save(changeStateForm);
    }

    public void saveUser(Requestbody requestbody, Map<String, Object> koperasi) {
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
        anggotaKoperasi.setKoperasi(koperasiRepository.getOne((Integer) koperasi.get("id")));
        anggotaKoperasi.setUser(user);

        angotaKoperasiRepository.save(anggotaKoperasi);
    }

    public void savePengaturanPeminjaman(Map<String, Object> koperasi, Requestbody requestbody) {
        PengaturanPinjaman pengaturanPinjaman = new PengaturanPinjaman();
        pengaturanPinjaman.setBungaPinjaman(requestbody.getBungaPinjaman() / 100);
        pengaturanPinjaman.setAmbangBatasDenda(requestbody.getAmbangBatasDenda());
        pengaturanPinjaman.setMaxTenor(requestbody.getMaxTenor());
        pengaturanPinjaman.setMinTenor(requestbody.getMinTenor());
        pengaturanPinjaman.setPersentaseDenda(requestbody.getPersentaseDenda() / 100);

        Koperasi kop = koperasiRepository.getOne((Integer) koperasi.get("id"));
        pengaturanPinjamanRepository.save(pengaturanPinjaman);
        if (koperasiPengaturanPinjamanRepository.existsByKoperasiAndStatus(kop, true)) {
            KoperasiPengaturanPinjaman koperasiPengaturanPinjaman = koperasiPengaturanPinjamanRepository.getFirstByKoperasiAndStatus(kop, true).get();
            KoperasiPengaturanPinjaman update = koperasiPengaturanPinjamanRepository.getOne(koperasiPengaturanPinjaman.getId());
            update.setStatus(false);
            koperasiPengaturanPinjamanRepository.save(update);
        }
        KoperasiPengaturanPinjaman koperasiPengaturanPinjaman = new KoperasiPengaturanPinjaman();
        koperasiPengaturanPinjaman.setStatus(true);
        koperasiPengaturanPinjaman.setKoperasi(kop);
        koperasiPengaturanPinjaman.setPengaturanPinjaman(pengaturanPinjaman);
        koperasiPengaturanPinjamanRepository.save(koperasiPengaturanPinjaman);
    }

    public void checkDenda() {
        List<Angsuran> angsurans = angsuranRepository.getExistDenda();
        for (Angsuran angsuran : angsurans) {
            Double denda = angsuran.getDenda() == null ? 0.0 : angsuran.getDenda();
            String dd = angsuran.getTanggalJatuhTempo().toString().substring(0, 10);
            LocalDate l = LocalDate.parse(dd);
            LocalDateTime now1 = LocalDateTime.now();
            LocalDate now = LocalDate.of(now1.getYear(), now1.getMonthValue(), now1.getDayOfMonth());
            Long days = ChronoUnit.DAYS.between(l, now);
            Map<String, Object> pinjaman = pinjamanRepository.getFirstById(angsuran.getPinjaman().getId());
            Map<String, Object> pengaturanPinjaman = pengaturanPinjamanRepository.getFirstById((Integer) pinjaman.get("id_pengaturan_pinjaman"));
            long day = days + (Integer) pengaturanPinjaman.get("ambang_batas_denda");
            Double total = ((Double) pinjaman.get("jumlah_pinjaman") * (Double) pengaturanPinjaman.get("persentase_denda") / 100) * day;
            angsuran.setDenda(total);
            angsuran.setTotalTagihan(angsuran.getAngsuranPokok() + angsuran.getDenda() + angsuran.getBunga());
            angsuran.setTotalAngsuran(angsuran.getAngsuranPokok() + angsuran.getDenda() + angsuran.getBunga());
            angsuranRepository.save(angsuran);
            Boolean existNotif = notifikasiAnggotaRepository.checkExistBy(angsuran.getId());
            if (!existNotif || denda.intValue() != angsuran.getDenda().intValue()) {
                NotifikasiAnggota notifikasiAnggota = new NotifikasiAnggota();
                notifikasiAnggota.setIdAngsuran(angsuran.getId());
                Map<String, Object> getDataAngsuran = angsuranRepository.getDataPinjaman(angsuran.getId());
                notifikasiAnggota.setIdUser((Integer) getDataAngsuran.get("id_user"));
                notifikasiAnggota.setTanggalNotifikasi(new Date());
                notifikasiAnggota.setPesan("Angsuran anda yang ke " + angsuran.getUrutanKe() +
                        ". Dengan Kode Pinjaman #" + getDataAngsuran.get("kode_pinjaman") +
                        ". Dikenakan denda sebesar " + toIDR(total.intValue()));
                notifikasiAnggota.setStatus(false);
                notifikasiAnggotaRepository.save(notifikasiAnggota);
            }
        }
    }

    public String toIDR(Integer num) {
        String nums = String.format("%d", num);

        String ans = "";
        int coma = 0;
        for (int i = nums.length() - 1; i >= 0; i--) {
            ans = String.format("%s%c", ans, nums.charAt(i));
            if (coma == 2 && i != 0) {
                ans = String.format("%s,", ans);
                coma = 0;
            } else {
                coma++;
            }
        }
        String res = "Rp ";
        for (int i = ans.length() - 1; i >= 0; i--) {
            res = String.format("%s%c", res, ans.charAt(i));
        }
        return res;
    }

    public Map<String, Object> getLaporanPemasukanDanLaba(Map<String, Object> koperasi, Date dateFrom, Date dateTo) {
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.get("nama_koperasi"));
        Long realisasiJasa = angsuranRepository.getRealisasiJasaDateFromTo((Integer) koperasi.get("id"), dateFrom, dateTo);
        data.put("realisasiJasa", realisasiJasa);
        Long denda = angsuranRepository.getDenda((Integer) koperasi.get("id"), dateFrom, dateTo);
        data.put("tunggakan", denda);
        Long simpananPokok = transaksiSimpananRepository.getTransaksi((Integer) koperasi.get("id"), 1, 1, dateFrom, dateTo);
        data.put("simpananPokok", simpananPokok);
        Long simpananWajib = transaksiSimpananRepository.getTransaksi((Integer) koperasi.get("id"), 1, 2, dateFrom, dateTo);
        data.put("simpananWajib", simpananWajib);
        Long simpananSukarela = transaksiSimpananRepository.getTransaksi((Integer) koperasi.get("id"), 1, 3, dateFrom, dateTo);
        data.put("simpananSukarela", simpananSukarela);
        return data;
    }

    public Map<String, Object> getLaporanTransaksiSimpanan(Map<String, Object> koperasi, Date dateFrom, Date dateTo) {
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.get("nama_koperasi"));
        List<Map<String, Object>> transaksiSimpanans = transaksiSimpananRepository.getTransaksiSimpananLaporan((Integer) koperasi.get("id"), dateFrom, dateTo);
        List<Map<String, Object>> res = new ArrayList<>();
        for (Map<String, Object> transaksiSimpanan : transaksiSimpanans) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("noTransaksi", transaksiSimpanan.get("kode_transaksi"));
            temp.put("nama", transaksiSimpanan.get("first_name") + " " + transaksiSimpanan.get("last_name"));
            temp.put("tipeTransaksi", ((Integer) transaksiSimpanan.get("jenis_transaksi") == 1 ? "Setor Dana" : "Tarik Dana"));
            temp.put("produk", ((Integer) transaksiSimpanan.get("jenis_simpanan") == 1 ? "Simpanan Pokok" : ((Integer) transaksiSimpanan.get("jenis_simpanan") == 2 ? "Simpanan Wajib" : "Simpanan Sukarela")));
            temp.put("tglTransaksi", transaksiSimpanan.get("created_at"));
            temp.put("nominal", transaksiSimpanan.get("jumlah_transaksi"));
            res.add(temp);
        }
        data.put("dataTable", res);
        return data;
    }

    public Map<String, Object> getLaporanTransaksiPinjaman(Map<String, Object> koperasi, Date dateFrom, Date dateTo) {
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.get("nama_koperasi"));
        List<Map<String, Object>> transaksiPinjaman = pinjamanRepository.getLaporanPinjaman((Integer) koperasi.get("id"), dateFrom, dateTo);
        List<Map<String, Object>> res = new ArrayList<>();
        double totPinjaman = 0.0, totLaba = 0.0;
        for (Map<String, Object> pinjaman : transaksiPinjaman) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("noTransaksi", pinjaman.get("kode_pinjaman"));
            temp.put("nama", pinjaman.get("first_name") + " " + pinjaman.get("last_name"));
            temp.put("jaminan", pinjaman.get("jaminan"));
            temp.put("jumlahPinjaman", pinjaman.get("jumlah_pinjaman"));
            temp.put("tenor", pinjaman.get("tenor"));
            totPinjaman += (Double) pinjaman.get("jumlah_pinjaman");
            double total = 0.0;
            List<Map<String, Object>> angsurans = angsuranRepository.getAllByPinjaman((Integer) pinjaman.get("id"));
            for (Map<String, Object> angsuran : angsurans) {
                total += (Double) angsuran.get("bunga") + (Double) angsuran.get("denda");
            }
            totLaba += total;
            temp.put("laba", total);
            temp.put("tglPengajuan", pinjaman.get("created_at"));
            temp.put("tglDiterimaPengajuan", pinjaman.get("date_pengajuan_diterima"));
            temp.put("tglSelesaiCicilan", (pinjaman.get("updated_at") == null ? "-" : pinjaman.get("updated_at")));
            res.add(temp);
        }
        data.put("totLaba", totLaba);
        data.put("totPinjaman", totPinjaman);
        data.put("dataTable", res);
        return data;
    }

    public Map<String, Object> getLaporanPemasukanProduk(Map<String, Object> koperasi, Date dateFrom, Date dateTo) {
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.get("nama_koperasi"));
        List<Map<String, Object>> produkMasuk = produkBaruRepository.getTransaksiProdukMasukLaporan((Integer) koperasi.get("id"), dateFrom, dateTo);
        data.put("totProdMasuk", produkBaruRepository.getTotalTransaksiProdukMasukLaporan((Integer) koperasi.get("id"), dateFrom, dateTo));
        data.put("dataTable", produkMasuk);
        return data;
    }

    public Map<String, Object> getLaporanPenjualanProduk(Map<String, Object> koperasi, Date dateFrom, Date dateTo) {
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.get("nama_koperasi"));
        List<Map<String, Object>> penjualanProduk = penjualanProdukRepository.getLaporanPenjualan((Integer) koperasi.get("id"), dateFrom, dateTo);
        data.put("totProdTerjual", penjualanProdukRepository.getTotalLaporanPenjualan((Integer) koperasi.get("id"), dateFrom, dateTo));
        data.put("totTerjual", penjualanProdukRepository.getTotalJual((Integer) koperasi.get("id"), dateFrom, dateTo));
        data.put("dataTable", penjualanProduk);
        return data;
    }

    public void buatLaporan(Integer id, Integer tahun, MultipartFile file) throws Exception {
        String pathLaporan = saveLaporan(file);
        DriveQuickstart driveQuickstart = new DriveQuickstart();
        File t = new File("");
        String[] split = file.getOriginalFilename().split("\\.");
        String extensiFile = split[split.length - 1];
        File files = new File(t.getAbsolutePath() + pathLaporan);
        pathLaporan = driveQuickstart.uploadSpreedSheet(files, extensiFile);
        files.delete();
        LaporanKoperasi laporanKoperasi = new LaporanKoperasi();
        laporanKoperasi.setCreatedAt(new Date());
        laporanKoperasi.setIdKoperasi(id);
        laporanKoperasi.setOriginalName(file.getOriginalFilename());
        laporanKoperasi.setPathLaporan(pathLaporan);
        laporanKoperasi.setTahunLaporan(tahun);
        laporanKoperasi.setStatus(1);
        laporanKoperasi.setUpdatedAt(new Date());
        laporanKoperasi.setExtensiFile(extensiFile);
        laporanKoperasiRepository.save(laporanKoperasi);
    }

    private String saveLaporan(MultipartFile files) {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
        String name = myDateObj.format(myFormatObj);

        File curFile = new File("");
        String helper = curFile.getAbsolutePath();
        String curDir = helper + "/backend/src/main/resources/static/laporan/";
        String pict = files.getOriginalFilename();
        Path path = Paths.get(curDir + pict);
        byte[] images = new byte[0];
        try {
            images = files.getBytes();
            Files.write(path, images);
            return "/backend/src/main/resources/static/laporan/" + pict;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveEditLaporan(Integer id, Requestbody requestbody) throws Exception {
        LaporanKoperasi laporanKoperasi = laporanKoperasiRepository.getOne(id);
        laporanKoperasi.setTahunLaporan(requestbody.getTahun());
        if (requestbody.getFiles() != null) {
            MultipartFile file = requestbody.getFiles();
            String pathLaporan = saveLaporan(file);
            DriveQuickstart driveQuickstart = new DriveQuickstart();
            File t = new File("");
            String[] split = file.getOriginalFilename().split("\\.");
            String extensiFile = split[split.length - 1];
            File files = new File(t.getAbsolutePath() + pathLaporan);
            pathLaporan = driveQuickstart.uploadSpreedSheet(files, extensiFile);
            files.delete();
            laporanKoperasi.setExtensiFile(extensiFile);
            laporanKoperasi.setPathLaporan(pathLaporan);
            laporanKoperasi.setOriginalName(file.getOriginalFilename());
        }
        laporanKoperasiRepository.save(laporanKoperasi);
    }
}
