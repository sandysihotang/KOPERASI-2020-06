package io.github.sandy.controller;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import io.github.sandy.ErrorCode.Err;
import io.github.sandy.additional.ExportExcel;
import io.github.sandy.gdrive.DriveQuickstart;
import io.github.sandy.gdrive.GmailQuickStart;
import io.github.sandy.model.*;
import io.github.sandy.repository.*;
import io.github.sandy.request.Requestbody;
import io.github.sandy.service.KoperasiService;
import io.github.sandy.service.MailSender;
import io.github.sandy.service.UserDetailServiceImpl;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class KoperasiController {
    @Autowired
    KoperasiRepository koperasiRepository;

    @Autowired
    KoperasiService koperasiService;

    @Autowired
    DetailUserRepository detailUserRepository;

    @Autowired
    ProdukBaruRepository produkBaruRepository;

    @Autowired
    UserRepository userDetailRepository;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DaftarAnggotaKoperasiRepository daftarAnggotaKoperasiRepository;

    @Autowired
    AktivasiSimpananRepository aktivasiSimpananRepository;

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
    AngsuranRepository angsuranRepository;

    @Autowired
    ProdukRepository produkRepository;

    @Autowired
    PenjualanProdukRepository penjualanProdukRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    NotifikasiAnggotaRepository notifikasiAnggotaRepository;

    @Autowired
    LaporanKoperasiRepository laporanKoperasiRepository;

    @RequestMapping(value = "/api/createkoperasi", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Err> createKoperasi(@ModelAttribute Requestbody requestbody, HttpServletRequest request) throws Exception {
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        koperasiService.createKoperasi(requestbody, user);

        return new ResponseEntity<>(new Err(200, ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/coba", method = RequestMethod.GET)
    public void cal() throws IOException, GeneralSecurityException, MessagingException {
        GmailQuickStart gmailQuickStart = new GmailQuickStart();
//        gmailQuickStart.call();
        Properties properties = new Properties();
        Session session = Session.getDefaultInstance(properties, null);
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.addFrom(new InternetAddress[]{new InternetAddress("TobaKo")});
        mimeMessage.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress("sandysihotang868@gmail.com")});
        mimeMessage.setSubject("Testing Subject");
        mimeMessage.setText("Testing Text");
        gmailQuickStart.sendMessage("me", mimeMessage);
    }

    @RequestMapping(value = "/api/getallkoperasi", method = RequestMethod.GET)
    public List<Map<String, Object>> get() throws Exception {
        List<Map<String, Object>> data = new ArrayList<>();
        List<Map<String, Object>> koperasis = koperasiRepository.findByIsHaveKoperasi();
        for (Map<String, Object> koperasi : koperasis) {
            Map<String, Object> res = new HashMap<>();
            res.put("namaKoperasi", koperasi.get("nama_koperasi"));
            res.put("id", koperasi.get("id"));
            res.put("jenisKoperasi", koperasi.get("jenis_koperasi"));
            res.put("namaPendiri", koperasi.get("nama_pendiri"));
            res.put("alamatKoperasi", koperasi.get("alamat_koperasi"));
            res.put("tahunBerdiriKoperasi", koperasi.get("tahun_berdiri_koperasi"));
            res.put("email", koperasi.get("email"));
            res.put("noIzinKoperasi", koperasi.get("no_izin_koperasi"));
            res.put("haveKoperasi", koperasi.get("have_koperasi"));
            if (koperasi.get("logo_koperasi") != null) {
                DriveQuickstart driveQuickstart = new DriveQuickstart();
                InputStream files = driveQuickstart.getFile((String) koperasi.get("logo_koperasi"));
//                FileInputStream file = new FileInputStream(files.get);
//                String ss = files.getFullFileExtension();
//                GenericUrl url = new GenericUrl();
                res.put("logoKoperasi", IOUtils.toByteArray(files));
                System.out.println();
            } else {
                res.put("logoKoperasi", null);
            }
            data.add(res);
        }
        return data;
    }

    @RequestMapping(value = "/api/getlabapengeluaranpemasukanmonth", method = RequestMethod.GET)
    public Map<String, Object> getLabaPengeluranPemasukan(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Map<String, Object> data = new HashMap<>();
        data.put("pemasukan", angsuranRepository.getRealisasiJasa((Integer) koperasi.get("id")) +
                (transaksiSimpananRepository.getJumlahYangMeminjam((Integer) koperasi.get("id"), 1, 1) - transaksiSimpananRepository.getJumlahYangMeminjam((Integer) koperasi.get("id"), 2, 1)) +
                (transaksiSimpananRepository.getJumlahYangMeminjam((Integer) koperasi.get("id"), 1, 2) - transaksiSimpananRepository.getJumlahYangMeminjam((Integer) koperasi.get("id"), 2, 2)) +
                transaksiSimpananRepository.getJumlahYangMeminjam((Integer) koperasi.get("id"), 1, 3) - transaksiSimpananRepository.getJumlahYangMeminjam((Integer) koperasi.get("id"), 2, 3));
        data.put("pengeluaran", angsuranRepository.getPengeluaran((Integer) koperasi.get("id")));
        data.put("laba", angsuranRepository.getRealisasiJasa((Integer) koperasi.get("id")));
        return data;
    }

    @RequestMapping(value = "/api/changestatekoperasi", method = RequestMethod.POST)
    public ResponseEntity<Err> changeState(@RequestBody Requestbody requestbody, HttpServletRequest request) throws GeneralSecurityException, IOException, MessagingException {
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        koperasiService.changeStateKoperasi(requestbody.getId(), requestbody.getText(), requestbody.isState());
        return new ResponseEntity<>(new Err(200, ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/getlaporanpemasukandanlaba", method = RequestMethod.POST)
    public Map<String, Object> getLaporanPemasukanDanLaba(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return koperasiService.getLaporanPemasukanDanLaba(koperasi, new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo()));
    }

    @RequestMapping(value = "/api/getlaporantransaksisimpanan", method = RequestMethod.POST)
    public Map<String, Object> getLaporanTransaksiSimpanan(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return koperasiService.getLaporanTransaksiSimpanan(koperasi, new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo()));
    }

    @RequestMapping(value = "/api/getlaporantransaksipinjaman", method = RequestMethod.POST)
    public Map<String, Object> getLaporanTransaksiPinjaman(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return koperasiService.getLaporanTransaksiPinjaman(koperasi, new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo()));
    }

    @RequestMapping(value = "/api/getlaporanpemasukanproduk", method = RequestMethod.POST)
    public Map<String, Object> getLaporanPemasukanProduk(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return koperasiService.getLaporanPemasukanProduk(koperasi, new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo()));
    }

    @RequestMapping(value = "/api/getlaporanpenjualanproduk", method = RequestMethod.POST)
    public Map<String, Object> getLaporanPenjualanProduk(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return koperasiService.getLaporanPenjualanProduk(koperasi, new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo()));
    }

    @RequestMapping(value = "/api/getlaporanpenjualanproduk/download", method = RequestMethod.POST)
    public void getLaporanPenjualanDownload(@RequestBody Requestbody requestbody, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExportExcel exportExcel = new ExportExcel();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=pemasukanbarang.xlsx");
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Map<String, Object> data = koperasiService.getLaporanPenjualanProduk(koperasi, new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo()));
        data.put("periode", requestbody.getDateFrom() + " (s/d) " + requestbody.getDateTo());
        ByteArrayInputStream byteArray = exportExcel.downloadPenjualanProduk(data);
        IOUtils.copy(byteArray, response.getOutputStream());
    }

    @RequestMapping(value = "/api/getanggotalaporan", method = RequestMethod.GET)
    public Map<String, Object> getLaporanAnggota(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.get("nama_koperasi"));
        data.put("pengaturanField", daftarAnggotaKoperasiRepository.findByKoperasiId((Integer) koperasi.get("id")).get("pattern_field"));
        data.put("anggota", angotaKoperasiRepository.getALlAnggotaKoperasiEnable((Integer) koperasi.get("id"), true));
        data.put("totalAnggota", angotaKoperasiRepository.getCountAnggota((Integer) koperasi.get("id"), true));
        return data;
    }

    @RequestMapping(value = "/api/getlaporankoperasiforanggota", method = RequestMethod.GET)
    public Map<String, Object> getLaporanKoperasiAnggota(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> anggota = angotaKoperasiRepository.getByFirstByIdUser((Integer) user.get("id"));
        Map<String, Object> data = new HashMap<>();
        Boolean exist = laporanKoperasiRepository.existsByIdKoperasi((Integer) anggota.get("id_koperasi"));
        data.put("exist", exist);
        if (exist) {
            data.put("datatable", laporanKoperasiRepository.getAllByKoperasiAndStatus((Integer) anggota.get("id_koperasi")));
        }
        return data;
    }

    @RequestMapping(value = "/api/getlaporanproduk", method = RequestMethod.GET)
    public Map<String, Object> getLaporanProduk(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.get("nama_koperasi"));
        data.put("dataTable", produkRepository.getAllData((Integer) koperasi.get("id")));
        data.put("jumlahProduk", produkRepository.getTotalProduk((Integer) koperasi.get("id")));
        return data;
    }

    @RequestMapping(value = "/api/getlaporanproduk/download", method = RequestMethod.GET)
    public void getLaporanProdukDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExportExcel exportExcel = new ExportExcel();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=pemasukanbarang.xlsx");
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.get("nama_koperasi"));
        data.put("dataTable", produkRepository.getAllData((Integer) koperasi.get("id")));
        data.put("jumlahProduk", produkRepository.getTotalProduk((Integer) koperasi.get("id")));
        ByteArrayInputStream byteArray = exportExcel.downloadLaporanProduk(data);
        IOUtils.copy(byteArray, response.getOutputStream());
    }

    @RequestMapping(value = "/api/getanggotalaporan/download", method = RequestMethod.GET)
    public void getLaporanAnggotaDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExportExcel exportExcel = new ExportExcel();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=pemasukanbarang.xlsx");
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.get("nama_koperasi"));
        data.put("pengaturanField", daftarAnggotaKoperasiRepository.findByKoperasiId((Integer) koperasi.get("id")).get("pattern_field"));
        data.put("anggota", angotaKoperasiRepository.getALlAnggotaKoperasiEnable((Integer) koperasi.get("id"), true));
        data.put("totalAnggota", angotaKoperasiRepository.getCountAnggota((Integer) koperasi.get("id"), true));
        ByteArrayInputStream byteArray = exportExcel.downloadAnggotaAktif(data);
        IOUtils.copy(byteArray, response.getOutputStream());
    }

    @RequestMapping(value = "/api/getlaporantransaksisimpanan/download", method = RequestMethod.POST)
    public void getLaporanTransaksiSimpananDownload(@RequestBody Requestbody requestbody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExportExcel exportExcel = new ExportExcel();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=pemasukanbarang.xlsx");
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Map<String, Object> data = koperasiService.getLaporanTransaksiSimpanan(koperasi, new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo()));
        data.put("periode", requestbody.getDateFrom() + " (s/d) " + requestbody.getDateTo());
        ByteArrayInputStream byteArray = exportExcel.downloadTransaksiSimpanan(data);
        IOUtils.copy(byteArray, response.getOutputStream());
    }

    @RequestMapping(value = "/api/getlaporanpemasukanproduk/download", method = RequestMethod.POST)
    public void getLaporanPemasukanProdukDownload(@RequestBody Requestbody requestbody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExportExcel exportExcel = new ExportExcel();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=pemasukanbarang.xlsx");
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.get("nama_koperasi"));
        List<Map<String, Object>> produkMasuk = produkBaruRepository.getTransaksiProdukMasukLaporan((Integer) koperasi.get("id"), new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo()));
        data.put("totProdMasuk", produkBaruRepository.getTotalTransaksiProdukMasukLaporan((Integer) koperasi.get("id"), new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo())));
        data.put("dataTable", produkMasuk);
        data.put("periode", requestbody.getDateFrom() + " (s/d) " + requestbody.getDateTo());
        ByteArrayInputStream byteArray = exportExcel.downloadPemasukanProduk(data);
        IOUtils.copy(byteArray, response.getOutputStream());
    }

    @RequestMapping(value = "/api/getlaporantransaksipinjaman/download", method = RequestMethod.POST)
    public void getLaporanPinjamanDownload(@RequestBody Requestbody requestbody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExportExcel exportExcel = new ExportExcel();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=pemasukanbarang.xlsx");
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Map<String, Object> data = koperasiService.getLaporanTransaksiPinjaman(koperasi, new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo()));
        data.put("periode", requestbody.getDateFrom() + " (s/d) " + requestbody.getDateTo());
        ByteArrayInputStream byteArray = exportExcel.downloadPinjaman(data);
        IOUtils.copy(byteArray, response.getOutputStream());
    }

    @RequestMapping(value = "/api/getlaporanpemasukandanlaba/download", method = RequestMethod.POST)
    public void getLaporanPemasukanDanLabaDownload(@RequestBody Requestbody requestbody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExportExcel exportExcel = new ExportExcel();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=pemasukanbarang.xlsx");
        Principal principal = request.getUserPrincipal();

        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Map<String, Object> data = koperasiService.getLaporanPemasukanDanLaba(koperasi, new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo()));
        data.put("periode", requestbody.getDateFrom() + " (s/d) " + requestbody.getDateTo());

        ByteArrayInputStream byteArray = exportExcel.downloadLabaDanMasuk(data);
        IOUtils.copy(byteArray, response.getOutputStream());
    }

    @RequestMapping(value = "/api/isHaveField", method = RequestMethod.GET)
    public boolean checkIshaveFieldMember(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return (boolean) koperasi.get("have_field_register_member");
    }

    @RequestMapping(value = "/api/getlaporanusersimpanan", method = RequestMethod.GET)
    public Map<String, Object> getDataLaporanSimpananUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> usera = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) usera.get("id"));
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.get("nama_koperasi"));
        List<Map<String, Object>> users = userRepository.findByKoperasiforReport((Integer) koperasi.get("id"));
        List<Map<String, Object>> res = new ArrayList<>();
        for (Map<String, Object> user : users) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("name", user.get("first_name") + " " + user.get("last_name"));
            temp.put("pokok", aktivasiSimpananRepository.getTransaksi((Integer) user.get("id"), 1));
            temp.put("wajib", aktivasiSimpananRepository.getTransaksi((Integer) user.get("id"), 2));
            temp.put("sukarela", aktivasiSimpananRepository.getTransaksi((Integer) user.get("id"), 3));
            res.add(temp);
        }
        data.put("dataTable", res);
        data.put("pokok", aktivasiSimpananRepository.getTransaksiKoperasi((Integer) koperasi.get("id"), 1));
        data.put("wajib", aktivasiSimpananRepository.getTransaksiKoperasi((Integer) koperasi.get("id"), 2));
        data.put("sukarela", aktivasiSimpananRepository.getTransaksiKoperasi((Integer) koperasi.get("id"), 3));
        return data;
    }

    @RequestMapping(value = "/api/getlaporansimpanan/download", method = RequestMethod.GET)
    public void getLaporanSimpananDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExportExcel exportExcel = new ExportExcel();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=pemasukanbarang.xlsx");
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> usera = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) usera.get("id"));
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.get("nama_koperasi"));
        List<Map<String, Object>> users = userRepository.findByKoperasiforReport((Integer) koperasi.get("id"));
        List<Map<String, Object>> res = new ArrayList<>();
        for (Map<String, Object> user : users) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("name", user.get("first_name") + " " + user.get("last_name"));
            temp.put("pokok", aktivasiSimpananRepository.getTransaksi((Integer) user.get("id"), 1));
            temp.put("wajib", aktivasiSimpananRepository.getTransaksi((Integer) user.get("id"), 2));
            temp.put("sukarela", aktivasiSimpananRepository.getTransaksi((Integer) user.get("id"), 3));
            res.add(temp);
        }
        data.put("dataTable", res);
        data.put("pokok", aktivasiSimpananRepository.getTransaksiKoperasi((Integer) koperasi.get("id"), 1));
        data.put("wajib", aktivasiSimpananRepository.getTransaksiKoperasi((Integer) koperasi.get("id"), 2));
        data.put("sukarela", aktivasiSimpananRepository.getTransaksiKoperasi((Integer) koperasi.get("id"), 3));

        ByteArrayInputStream byteArray = exportExcel.downloadSimpanan(data);
        IOUtils.copy(byteArray, response.getOutputStream());
    }

    @RequestMapping(value = "/api/getanggotaaknonak", method = RequestMethod.GET)
    public HashMap<String, Integer> getAngotaAktifNonAktif(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        HashMap<String, Integer> data = new HashMap<>();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Integer aktif = angotaKoperasiRepository.getCountJumlahAnggota((Integer) koperasi.get("id"), true);
        Integer nonAktif = angotaKoperasiRepository.getCountJumlahAnggota((Integer) koperasi.get("id"), false);
        data.put("aktif", aktif);
        data.put("nonAktif", nonAktif);

        return data;
    }

    @RequestMapping(value = "/api/getsimpanananggota", method = RequestMethod.GET)
    public HashMap<String, Integer> getSimpanan(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        HashMap<String, Integer> data = new HashMap<>();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        data.put("pokok", transaksiSimpananRepository.getJumlahYangMeminjam((Integer) koperasi.get("id"), 1, 1) - transaksiSimpananRepository.getJumlahYangMeminjam((Integer) koperasi.get("id"), 2, 1));
        data.put("wajib", transaksiSimpananRepository.getJumlahYangMeminjam((Integer) koperasi.get("id"), 1, 2) - transaksiSimpananRepository.getJumlahYangMeminjam((Integer) koperasi.get("id"), 2, 2));
        data.put("sukarela", transaksiSimpananRepository.getJumlahYangMeminjam((Integer) koperasi.get("id"), 1, 3) - transaksiSimpananRepository.getJumlahYangMeminjam((Integer) koperasi.get("id"), 2, 3));
        return data;
    }

    @RequestMapping(value = "/api/produkkoperasi", method = RequestMethod.GET)
    public HashMap<String, Integer> getKoperasiDashboard(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        HashMap<String, Integer> data = new HashMap<>();
        data.put("totalproduk", (produkRepository.existsByKoperasi((Integer) koperasi.get("id")) ? produkRepository.getTotalProduk((Integer) koperasi.get("id")) : 0));
        data.put("totalprodukterjual", (penjualanProdukRepository.existsByKoperasiAndStatus((Integer) koperasi.get("id"), true) ?
                penjualanProdukRepository.getJumlahTerjual((Integer) koperasi.get("id"), true) : 0));
        data.put("totaluangprodukterjual", (penjualanProdukRepository.existsByKoperasiAndStatus((Integer) koperasi.get("id"), true) ?
                penjualanProdukRepository.getJumlahUangTerjualAnggota((Integer) koperasi.get("id")) +
                        penjualanProdukRepository.getJumlahUangTerjualNonAnggota((Integer) koperasi.get("id"))
                : 0));

        return data;
    }

    @RequestMapping(value = "/api/getpinjamanfordashboard", method = RequestMethod.GET)
    public HashMap<String, Integer> getPinjamanForDashboard(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        HashMap<String, Integer> data = new HashMap<>();
        data.put("pinjamantersalur", (pinjamanRepository.existsByKoperasiAndStatus((Integer) koperasi.get("id"), 2) ? pinjamanRepository.getPinjaman((Integer) koperasi.get("id"), 2) : 0));
        data.put("pinjamanterbayar", (pinjamanRepository.existsByKoperasiAndStatus((Integer) koperasi.get("id"), 6) ? pinjamanRepository.getPinjaman((Integer) koperasi.get("id"), 6) : 0));
        data.put("pinjamanJatuhTempo", pinjamanRepository.getPinjamanJatuhTempo((Integer) koperasi.get("id")));
        return data;
    }

    @RequestMapping(value = "/api/getpinjamanfortransasaksi", method = RequestMethod.GET)
    public HashMap<String, Integer> getPinjamanForTransaksi(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        HashMap<String, Integer> data = new HashMap<>();
        data.put("pinjamanbelumbayar", pinjamanRepository.getpinjamanbelumbayar((Integer) koperasi.get("id")));
        data.put("pinjamanterbayar", pinjamanRepository.getpinjamanSudahBayarbelumbayar((Integer) koperasi.get("id")));
        data.put("pinjamanJatuhTempo", pinjamanRepository.getPinjamanJatuhTempo((Integer) koperasi.get("id")));
        return data;
    }


    @RequestMapping(value = "/api/getcolumnmember", method = RequestMethod.GET)
    public String getcolumn(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Map<String, Object> pattern = daftarAnggotaKoperasiRepository.findByKoperasiId((Integer) koperasi.get("id"));
        return (String) pattern.get("pattern_field");
    }

    @RequestMapping(value = "/api/getcolumnmember/{id}", method = RequestMethod.GET)
    public String getColumnPersetujuan(@PathVariable("id") int id) {
        Map<String, Object> pinjaman = pinjamanRepository.getFirstById(id);
        Map<String, Object> pattern = daftarAnggotaKoperasiRepository.findByKoperasiId((Integer) pinjaman.get("id_koperasi"));
        return (String) pattern.get("pattern_field");
    }

    @RequestMapping(value = "/api/getdatamember", method = RequestMethod.GET)
    public Set<Map<String, Object>> getData(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        return angotaKoperasiRepository.getALlAnggotaKoperasiEnable((Integer) koperasi.get("id"), true);
    }

    @RequestMapping(value = "/api/getdatamember/{id}", method = RequestMethod.GET)
    public Set<Map<String, Object>> getData(@PathVariable("id") int id) {
        Map<String, Object> pinjaman = pinjamanRepository.getFirstById(id);
        return angotaKoperasiRepository.getByUser((Integer) pinjaman.get("id_user"));
    }

    @RequestMapping(value = "/api/saveanggota", method = RequestMethod.POST)
    public ResponseEntity<Err> saveMemberKoperasi(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Err data = userDetailService.checkAnggota(requestbody);
        if (data.getErrCode() == 404) {
            return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
        }
        data = userDetailService.searchByUsername(requestbody.getUsername());
        if (data.getErrCode() == 403) {
            return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
        }
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        koperasiService.saveUser(requestbody, koperasi);
        return new ResponseEntity<>(new Err(200, ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/getstatekoperasi", method = RequestMethod.GET)
    public Map<String, Object> getStateKoperasi(HttpServletRequest request) throws Exception {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Map<String, Object> data = new HashMap<>();
        data.put("nama", koperasi.get("nama_koperasi"));
        if (koperasi.get("logo_koperasi") != null) {
            DriveQuickstart driveQuickstart = new DriveQuickstart();
            InputStream files = driveQuickstart.getFile((String) koperasi.get("logo_koperasi"));
            data.put("logoKoperasi", IOUtils.toByteArray(files));
        } else {
            data.put("logoKoperasi", null);
        }
        return data;
    }

    @RequestMapping(value = "/api/getnamekoperasi", method = RequestMethod.GET)
    public String getNameKoperasi(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);

        Map<String, Object> anggotaKoperasi = angotaKoperasiRepository.getByFirstByIdUser((Integer) user.get("id"));
        return angotaKoperasiRepository.getNameKoperasi((Integer) anggotaKoperasi.get("id"));
    }

    @RequestMapping(value = "/api/getnameanggota", method = RequestMethod.GET)
    public Map<String, Object> getNameMember(HttpServletRequest request) throws Exception {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> userDetail = detailUserRepository.findUserDetail((Integer) user.get("id"));
        data.put("name", userDetail.get("first_name") + " " + userDetail.get("last_name"));
        Map<String, Object> anggotaKoperasi = angotaKoperasiRepository.getFirstByUser((Integer) user.get("id"));
        Map<String, Object> koperasi = koperasiRepository.getKoperasiID((Integer) anggotaKoperasi.get("id_koperasi"));
        if (koperasi.get("logo_koperasi") != null) {
            DriveQuickstart driveQuickstart = new DriveQuickstart();
            InputStream files = driveQuickstart.getFile((String) koperasi.get("logo_koperasi"));
            data.put("logoKoperasi", IOUtils.toByteArray(files));
        } else {
            data.put("logoKoperasi", null);
        }
        return data;
    }

    @RequestMapping(value = "/api/getpengaturanpinjaman", method = RequestMethod.GET)
    public Map<String, Object> getPengaturanPeminjaman(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        if (koperasiPengaturanPinjamanRepository.existsByKoperasiAndStatusInPengaturan((Integer) koperasi.get("id"), true)) {
            Map<String, Object> koperasiPengaturanPinjaman = koperasiPengaturanPinjamanRepository.getFirstByKoperasiAndStatusInPengaturan((Integer) koperasi.get("id"), true);
            return pengaturanPinjamanRepository.getFirstById((Integer) koperasiPengaturanPinjaman.get("id_pengaturan"));
        }
        return null;
    }

    @RequestMapping(value = "/api/getpengaturanpinjamanreqpinjaman", method = RequestMethod.GET)
    public Map<String, Object> getPengaturanPeminjamanForRequestPinjaman(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> anggotaKoperasi = angotaKoperasiRepository.getByFirstByIdUser((Integer) user.get("id"));
        if (koperasiPengaturanPinjamanRepository.existsByKoperasiAndStatusInPengaturan((Integer) anggotaKoperasi.get("id_koperasi"), true)) {
            Map<String, Object> koperasiPengaturanPinjaman = koperasiPengaturanPinjamanRepository.getFirstByKoperasiAndStatusInPengaturan((Integer) anggotaKoperasi.get("id_koperasi"), true);
            return pengaturanPinjamanRepository.getFirstById((Integer) koperasiPengaturanPinjaman.get("id_pengaturan"));
        }
        return null;
    }

    @RequestMapping(value = "/api/savepengaturanpinjaman", method = RequestMethod.POST)
    public void savePengaturanPeminjaman(HttpServletRequest request, @RequestBody Requestbody requestbody) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        koperasiService.savePengaturanPeminjaman(koperasi, requestbody);
    }

    @RequestMapping(value = "/api/notifikasianggota", method = RequestMethod.GET)
    public Map<String, Object> savePengaturanPeminjaman(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Boolean exist = notifikasiAnggotaRepository.checkExistByUser((Integer) user.get("id"));
        Map<String, Object> data = new HashMap<>();
        data.put("exist", exist);
        if (exist) {
            data.put("total", notifikasiAnggotaRepository.getTotal((Integer) user.get("id")));
        }
        return data;
    }

    @RequestMapping(value = "/api/getnotifuser", method = RequestMethod.GET)
    public Map<String, Object> getNotif(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Boolean exist = notifikasiAnggotaRepository.checkExistByUserHome((Integer) user.get("id"));
        Map<String, Object> data = new HashMap<>();
        data.put("exist", exist);
        if (exist) {
            data.put("notif", notifikasiAnggotaRepository.getNotifikasiByUser((Integer) user.get("id")));
        }
        return data;
    }

    @RequestMapping(value = "/api/changestatusnotif", method = RequestMethod.GET)
    public void ubahStatusNotifikasi(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        notifikasiAnggotaRepository.ubahStatus((Integer) user.get("id"));
    }

    @RequestMapping(value = "/api/nonactivememberkoperasi/{id}", method = RequestMethod.PUT)
    public void nonAktifkanAnggota(@PathVariable("id") Integer id) throws GeneralSecurityException, IOException, MessagingException {
        AnggotaKoperasi anggotaKoperasi = angotaKoperasiRepository.getOne(id);
        User user = userRepository.getOne(anggotaKoperasi.getUser().getId());
        user.setEnabled(false);
        userRepository.save(user);
        MailSender mailSender = new MailSender();
        mailSender.sendEmailNonActiveAccountMember(user.getEmail(), "Account anda telah di nonaktifkan");

    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true)
class Schedule {
    private static final Logger log = LoggerFactory.getLogger(Schedule.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    KoperasiService koperasiService;

    @Scheduled(fixedRate = 43200000L)
    public void checkDenda() {
        System.out.println("Check Denda");
        koperasiService.checkDenda();
    }
}
