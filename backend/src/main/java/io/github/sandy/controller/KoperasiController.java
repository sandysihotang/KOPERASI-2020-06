package io.github.sandy.controller;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import io.github.sandy.ErrorCode.Err;
import io.github.sandy.additional.ExportExcel;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    @RequestMapping(value = "/api/createkoperasi", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Err> createKoperasi(@ModelAttribute Requestbody requestbody, HttpServletRequest request) throws Exception {
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        koperasiService.createKoperasi(requestbody, user);

        return new ResponseEntity<>(new Err(200, ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/getallkoperasi", method = RequestMethod.GET)
    public List<Map<String, Object>> get() throws IOException {
        List<Map<String, Object>> data = new ArrayList<>();
        List<Koperasi> koperasis = koperasiRepository.findByIsHaveKoperasi();
        for (Koperasi koperasi : koperasis) {
            Map<String, Object> res = new HashMap<>();
            res.put("namaKoperasi", koperasi.getNamaKoperasi());
            res.put("id", koperasi.getId());
            res.put("jenisKoperasi", koperasi.getJenisKoperasi());
            res.put("namaPendiri", koperasi.getNamaPendiri());
            res.put("alamatKoperasi", koperasi.getAlamatKoperasi());
            res.put("tahunBerdiriKoperasi", koperasi.getTahunBerdiriKoperasi());
            res.put("email", koperasi.getEmail());
            res.put("noIzinKoperasi", koperasi.getNoIzinKoperasi());
            res.put("haveKoperasi", koperasi.getUser().getHaveKoperasi());
            if (koperasi.getLogoKoperasi() != null) {
                File files = new File("");
                FileInputStream file = new FileInputStream(files.getAbsoluteFile() + koperasi.getLogoKoperasi());
                res.put("logoKoperasi", IOUtils.toByteArray(file));
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
        User user = userRepository.findByUsername(uname).get();
        Koperasi koperasi = koperasiRepository.findFirstByUser(user);
        System.out.println(angsuranRepository.getRealisasiJasa(koperasi.getId()));
        Map<String, Object> data = new HashMap<>();
        data.put("pemasukan", angsuranRepository.getRealisasiJasa(koperasi.getId()) +
                (transaksiSimpananRepository.getJumlahYangMeminjam(koperasi.getId(), 1, 1) - transaksiSimpananRepository.getJumlahYangMeminjam(koperasi.getId(), 2, 1)) +
                (transaksiSimpananRepository.getJumlahYangMeminjam(koperasi.getId(), 1, 2) - transaksiSimpananRepository.getJumlahYangMeminjam(koperasi.getId(), 2, 2)) +
                transaksiSimpananRepository.getJumlahYangMeminjam(koperasi.getId(), 1, 3) - transaksiSimpananRepository.getJumlahYangMeminjam(koperasi.getId(), 2, 3));
        data.put("pengeluaran", angsuranRepository.getPengeluaran(koperasi.getId()));
        data.put("laba", angsuranRepository.getRealisasiJasa(koperasi.getId()));
        return data;
    }

    @RequestMapping(value = "/api/changestatekoperasi", method = RequestMethod.POST)
    public ResponseEntity<Err> changeState(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        koperasiService.changeStateKoperasi(requestbody.getId(), requestbody.isState());
        return new ResponseEntity<>(new Err(200, ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/getlaporanpemasukandanlaba", method = RequestMethod.POST)
    public Map<String, Object> getLaporanPemasukanDanLaba(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();

        return koperasiService.getLaporanPemasukanDanLaba(user.getKoperasi(), new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo()));
    }

    @RequestMapping(value = "/api/getlaporantransaksisimpanan", method = RequestMethod.POST)
    public Map<String, Object> getLaporanTransaksiSimpanan(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();

        return koperasiService.getLaporanTransaksiSimpanan(user.getKoperasi(), new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo()));
    }

    @RequestMapping(value = "/api/getlaporantransaksipinjaman", method = RequestMethod.POST)
    public Map<String, Object> getLaporanTransaksiPinjaman(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();

        return koperasiService.getLaporanTransaksiPinjaman(user.getKoperasi(), new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo()));
    }

    @RequestMapping(value = "/api/getanggotalaporan", method = RequestMethod.GET)
    public Map<String, Object> getLaporanAnggota(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", user.getKoperasi().getNamaKoperasi());
        data.put("pengaturanField", daftarAnggotaKoperasiRepository.findByKoperasiId(user.getKoperasi().getId()).get().getPatternField());
        data.put("anggota", angotaKoperasiRepository.getALlAnggotaKoperasiEnable(user.getKoperasi().getId(), true));
        data.put("totalAnggota", angotaKoperasiRepository.getCountAnggota(user.getKoperasi().getId(), true));
        return data;
    }

    @RequestMapping(value = "/api/getanggotalaporan/download", method = RequestMethod.GET)
    public void getLaporanAnggotaDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExportExcel exportExcel = new ExportExcel();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=pemasukanbarang.xlsx");
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", user.getKoperasi().getNamaKoperasi());
        data.put("pengaturanField", daftarAnggotaKoperasiRepository.findByKoperasiId(user.getKoperasi().getId()).get().getPatternField());
        data.put("anggota", angotaKoperasiRepository.getALlAnggotaKoperasiEnable(user.getKoperasi().getId(), true));
        data.put("totalAnggota", angotaKoperasiRepository.getCountAnggota(user.getKoperasi().getId(), true));
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
        User user = userRepository.findByUsername(uname).get();

        Map<String, Object> data = koperasiService.getLaporanTransaksiSimpanan(user.getKoperasi(), new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo()));
        data.put("periode", requestbody.getDateFrom() + " (s/d) " + requestbody.getDateTo());
        ByteArrayInputStream byteArray = exportExcel.downloadTransaksiSimpanan(data);
        IOUtils.copy(byteArray, response.getOutputStream());
    }

    @RequestMapping(value = "/api/getlaporantransaksipinjaman/download", method = RequestMethod.POST)
    public void getLaporanPinjamanDownload(@RequestBody Requestbody requestbody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExportExcel exportExcel = new ExportExcel();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=pemasukanbarang.xlsx");
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();

        Map<String, Object> data = koperasiService.getLaporanTransaksiPinjaman(user.getKoperasi(), new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo()));
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
        User user = userRepository.findByUsername(uname).get();
        Map<String, Object> data = koperasiService.getLaporanPemasukanDanLaba(user.getKoperasi(), new Date(requestbody.getDateFrom()), new Date(requestbody.getDateTo()));
        data.put("namaKoperasi", user.getKoperasi().getNamaKoperasi());
        data.put("periode", requestbody.getDateFrom() + " (s/d) " + requestbody.getDateTo());

        ByteArrayInputStream byteArray = exportExcel.downloadLabaDanMasuk(data);
        IOUtils.copy(byteArray, response.getOutputStream());
    }

    @RequestMapping(value = "/api/isHaveField", method = RequestMethod.GET)
    public boolean checkIshaveFieldMember(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        Koperasi koperasi = koperasiRepository.getOne(userRepository.findByUsername(user).get().getKoperasi().getId());

        return koperasi.isHaveFieldRegisterMember();
    }

    @RequestMapping(value = "/api/getlaporanusersimpanan", method = RequestMethod.GET)
    public Map<String, Object> getDataLaporanSimpananUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Koperasi koperasi = koperasiRepository.getOne(userRepository.findByUsername(uname).get().getKoperasi().getId());
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.getNamaKoperasi());
        List<User> users = userRepository.findByKoperasiforReport(koperasi.getId());
        List<Map<String, Object>> res = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("name", user.getUserDetail().getFirstName() + " " + user.getUserDetail().getLastName());
            temp.put("pokok", aktivasiSimpananRepository.getTransaksi(user.getId(), 1));
            temp.put("wajib", aktivasiSimpananRepository.getTransaksi(user.getId(), 2));
            temp.put("sukarela", aktivasiSimpananRepository.getTransaksi(user.getId(), 3));
            res.add(temp);
        }
        data.put("dataTable", res);
        data.put("pokok", aktivasiSimpananRepository.getTransaksiKoperasi(koperasi.getId(), 1));
        data.put("wajib", aktivasiSimpananRepository.getTransaksiKoperasi(koperasi.getId(), 2));
        data.put("sukarela", aktivasiSimpananRepository.getTransaksiKoperasi(koperasi.getId(), 3));
        return data;
    }

    @RequestMapping(value = "/api/getlaporansimpanan/download", method = RequestMethod.GET)
    public void getLaporanSimpananDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExportExcel exportExcel = new ExportExcel();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=pemasukanbarang.xlsx");
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Koperasi koperasi = koperasiRepository.getOne(userRepository.findByUsername(uname).get().getKoperasi().getId());
        Map<String, Object> data = new HashMap<>();
        data.put("namaKoperasi", koperasi.getNamaKoperasi());
        List<User> users = userRepository.findByKoperasiforReport(koperasi.getId());
        List<Map<String, Object>> res = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("name", user.getUserDetail().getFirstName() + " " + user.getUserDetail().getLastName());
            temp.put("pokok", aktivasiSimpananRepository.getTransaksi(user.getId(), 1));
            temp.put("wajib", aktivasiSimpananRepository.getTransaksi(user.getId(), 2));
            temp.put("sukarela", aktivasiSimpananRepository.getTransaksi(user.getId(), 3));
            res.add(temp);
        }
        data.put("dataTable", res);
        data.put("pokok", aktivasiSimpananRepository.getTransaksiKoperasi(koperasi.getId(), 1));
        data.put("wajib", aktivasiSimpananRepository.getTransaksiKoperasi(koperasi.getId(), 2));
        data.put("sukarela", aktivasiSimpananRepository.getTransaksiKoperasi(koperasi.getId(), 3));

        ByteArrayInputStream byteArray = exportExcel.downloadSimpanan(data);
        IOUtils.copy(byteArray, response.getOutputStream());
    }

    @RequestMapping(value = "/api/getanggotaaknonak", method = RequestMethod.GET)
    public HashMap<String, Integer> getAngotaAktifNonAktif(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        HashMap<String, Integer> data = new HashMap<>();
        Koperasi koperasi = userRepository.findByUsername(user).get().getKoperasi();
        Integer aktif = angotaKoperasiRepository.getCountJumlahAnggota(koperasi.getId(), true);
        Integer nonAktif = angotaKoperasiRepository.getCountJumlahAnggota(koperasi.getId(), false);
        data.put("aktif", aktif);
        data.put("nonAktif", nonAktif);

        return data;
    }

    @RequestMapping(value = "/api/getsimpanananggota", method = RequestMethod.GET)
    public HashMap<String, Integer> getSimpanan(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        HashMap<String, Integer> data = new HashMap<>();
        Koperasi koperasi = userRepository.findByUsername(user).get().getKoperasi();
        data.put("pokok", transaksiSimpananRepository.getJumlahYangMeminjam(koperasi.getId(), 1, 1) - transaksiSimpananRepository.getJumlahYangMeminjam(koperasi.getId(), 2, 1));
        data.put("wajib", transaksiSimpananRepository.getJumlahYangMeminjam(koperasi.getId(), 1, 2) - transaksiSimpananRepository.getJumlahYangMeminjam(koperasi.getId(), 2, 2));
        data.put("sukarela", transaksiSimpananRepository.getJumlahYangMeminjam(koperasi.getId(), 1, 3) - transaksiSimpananRepository.getJumlahYangMeminjam(koperasi.getId(), 2, 3));
        return data;
    }

    @RequestMapping(value = "/api/produkkoperasi", method = RequestMethod.GET)
    public HashMap<String, Integer> getKoperasiDashboard(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        HashMap<String, Integer> data = new HashMap<>();
        Koperasi koperasi = userRepository.findByUsername(user).get().getKoperasi();
        data.put("totalproduk", (produkRepository.existsByKoperasi(koperasi) ? produkRepository.getTotalProduk(koperasi.getId()) : 0));
        data.put("totalprodukterjual", (penjualanProdukRepository.existsByKoperasiAndStatus(koperasi, true) ?
                penjualanProdukRepository.getJumlahTerjual(koperasi.getId(), true) : 0));
        data.put("totaluangprodukterjual", (penjualanProdukRepository.existsByKoperasiAndStatus(koperasi, true) ?
                penjualanProdukRepository.getJumlahUangTerjualAnggota(koperasi.getId()) +
                        penjualanProdukRepository.getJumlahUangTerjualNonAnggota(koperasi.getId())
                : 0));

        return data;
    }

    @RequestMapping(value = "/api/getpinjamanfordashboard", method = RequestMethod.GET)
    public HashMap<String, Integer> getPinjamanForDashboard(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        HashMap<String, Integer> data = new HashMap<>();
        Koperasi koperasi = userRepository.findByUsername(user).get().getKoperasi();
        data.put("pinjamantersalur", (pinjamanRepository.existsByKoperasiAndStatus(koperasi, 2) ? pinjamanRepository.getPinjaman(koperasi.getId(), 2) : 0));
        data.put("pinjamanterbayar", (pinjamanRepository.existsByKoperasiAndStatus(koperasi, 6) ? pinjamanRepository.getPinjaman(koperasi.getId(), 6) : 0));
        return data;
    }


    @RequestMapping(value = "/api/getcolumnmember", method = RequestMethod.GET)
    public String getcolumn(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        Koperasi koperasi = koperasiRepository.getOne(userRepository.findByUsername(user).get().getKoperasi().getId());
        String pattern = daftarAnggotaKoperasiRepository.findByKoperasiId(koperasi.getId()).get().getPatternField();
        return pattern;
    }

    @RequestMapping(value = "/api/getcolumnmember/{id}", method = RequestMethod.GET)
    public String getColumnPersetujuan(@PathVariable("id") int id) {
        Pinjaman pinjaman = pinjamanRepository.getFirstById(id);
        String pattern = daftarAnggotaKoperasiRepository.findByKoperasiId(pinjaman.getKoperasi().getId()).get().getPatternField();
        return pattern;
    }

    @RequestMapping(value = "/api/getdatamember", method = RequestMethod.GET)
    public Set<AnggotaKoperasi> getData(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        Koperasi koperasi = koperasiRepository.getOne(userRepository.findByUsername(user).get().getKoperasi().getId());
        return angotaKoperasiRepository.getALlAnggotaKoperasiEnable(koperasi.getId(), true);
    }

    @RequestMapping(value = "/api/getdatamember/{id}", method = RequestMethod.GET)
    public Set<AnggotaKoperasi> getData(@PathVariable("id") int id) {
        Pinjaman pinjaman = pinjamanRepository.getFirstById(id);
        return angotaKoperasiRepository.getByUser(pinjaman.getUser());
    }

    @RequestMapping(value = "/api/saveanggota", method = RequestMethod.POST)
    public ResponseEntity<Err> saveMemberKoperasi(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Err data = userDetailService.check(requestbody);
        if (data.getErrCode() == 404) {
            return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
        }
        data = userDetailService.searchByUsername(requestbody.getUsername());
        if (data.getErrCode() == 403) {
            return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
        }
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        Koperasi koperasi = koperasiRepository.getOne(userRepository.findByUsername(user).get().getKoperasi().getId());
        koperasiService.saveUser(requestbody, koperasi);
        return new ResponseEntity<>(new Err(200, ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/getstatekoperasi", method = RequestMethod.GET)
    public Map<String, Object> getStateKoperasi(HttpServletRequest request) throws IOException {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();
        Map<String, Object> data = new HashMap<>();
        data.put("nama", user.getKoperasi().getNamaKoperasi());
        if (user.getKoperasi().getLogoKoperasi() != null) {
            File files = new File("");
            FileInputStream file = new FileInputStream(files.getAbsoluteFile() + user.getKoperasi().getLogoKoperasi());
            data.put("logoKoperasi", IOUtils.toByteArray(file));
        } else {
            data.put("logoKoperasi", null);
        }
        return data;
    }

    @RequestMapping(value = "/api/getnamekoperasi", method = RequestMethod.GET)
    public String getNameKoperasi(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();
        AnggotaKoperasi anggotaKoperasi = angotaKoperasiRepository.findFirstByUser(user).get();
        return anggotaKoperasi.getKoperasi().getNamaKoperasi();
    }

    @RequestMapping(value = "/api/getnameanggota", method = RequestMethod.GET)
    public Map<String, Object> getNameMember(HttpServletRequest request) throws Exception {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        Map<String, Object> data = new HashMap<>();
        data.put("name", user.getUserDetail().getFirstName() + " " + user.getUserDetail().getLastName());
        AnggotaKoperasi anggotaKoperasi = angotaKoperasiRepository.getFirstByUser(user);
        if (anggotaKoperasi.getKoperasi().getLogoKoperasi() != null) {
            File files = new File("");
            FileInputStream file = new FileInputStream(files.getAbsoluteFile() + anggotaKoperasi.getKoperasi().getLogoKoperasi());
            data.put("logoKoperasi", IOUtils.toByteArray(file));
        } else {
            data.put("logoKoperasi", null);
        }
        return data;
    }

    @RequestMapping(value = "/api/getpengaturanpinjaman", method = RequestMethod.GET)
    public PengaturanPinjaman getPengaturanPeminjaman(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        Koperasi koperasi = koperasiRepository.findFirstByUser(user);
        if (koperasiPengaturanPinjamanRepository.existsByKoperasiAndStatus(koperasi, true)) {
            KoperasiPengaturanPinjaman koperasiPengaturanPinjaman = koperasiPengaturanPinjamanRepository.getFirstByKoperasiAndStatus(koperasi, true).get();
            return pengaturanPinjamanRepository.findFirstById(koperasiPengaturanPinjaman.getPengaturanPinjaman().getId()).get();
        }
        return null;
    }

    @RequestMapping(value = "/api/getpengaturanpinjamanreqpinjaman", method = RequestMethod.GET)
    public PengaturanPinjaman getPengaturanPeminjamanForRequestPinjaman(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        AnggotaKoperasi anggotaKoperasi = angotaKoperasiRepository.findFirstByUser(user).get();
        if (koperasiPengaturanPinjamanRepository.existsByKoperasiAndStatus(anggotaKoperasi.getKoperasi(), true)) {
            KoperasiPengaturanPinjaman koperasiPengaturanPinjaman = koperasiPengaturanPinjamanRepository.getFirstByKoperasiAndStatus(anggotaKoperasi.getKoperasi(), true).get();
            return pengaturanPinjamanRepository.findFirstById(koperasiPengaturanPinjaman.getPengaturanPinjaman().getId()).get();
        }
        return null;
    }

    @RequestMapping(value = "/api/savepengaturanpinjaman", method = RequestMethod.POST)
    public void savePengaturanPeminjaman(HttpServletRequest request, @RequestBody Requestbody requestbody) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        Koperasi koperasi = koperasiRepository.findFirstByUser(user);
        koperasiService.savePengaturanPeminjaman(koperasi, requestbody);
    }

    @RequestMapping(value = "/api/nonactivememberkoperasi/{id}", method = RequestMethod.PUT)
    public void nonAktifkanAnggota(@PathVariable("id") Integer id) {
        AnggotaKoperasi anggotaKoperasi = angotaKoperasiRepository.getOne(id);
        User user = userRepository.getOne(anggotaKoperasi.getUser().getId());
        user.setEnabled(false);
        userRepository.save(user);
        MailSender mailSender = new MailSender();
//        mailSender.sendEmailNonActiveAccountMember(javaMailSender, user.getEmail(), "Account anda telah di nonaktifkan");

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
