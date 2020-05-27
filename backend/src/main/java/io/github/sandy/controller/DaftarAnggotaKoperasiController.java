package io.github.sandy.controller;

import com.google.api.services.drive.Drive;
import io.github.sandy.ErrorCode.Err;
import io.github.sandy.gdrive.DriveQuickstart;
import io.github.sandy.model.FieldDaftarAnggota;
import io.github.sandy.model.LaporanKoperasi;
import io.github.sandy.model.NotifikasiKoperasi;
import io.github.sandy.repository.*;
import io.github.sandy.request.Requestbody;
import io.github.sandy.service.KoperasiService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DaftarAnggotaKoperasiController {
    @Autowired
    DaftarAnggotaKoperasiRepository daftarAnggotaKoperasiRepository;

    @Autowired
    KoperasiService koperasiService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    KoperasiRepository koperasiRepository;

    @Autowired
    LaporanKoperasiRepository laporanKoperasiRepository;

    @Autowired
    NotifikasiKoperasiRepository notifikasiKoperasiRepository;

    @RequestMapping(value = "/ss", method = RequestMethod.GET)
    public List<FieldDaftarAnggota> get() {
        return daftarAnggotaKoperasiRepository.findAll();
    }

    @RequestMapping(value = "/api/saveformregister", method = RequestMethod.POST)
    public ResponseEntity<Err> saveFormRegisterMember(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        koperasiService.saveFormRegisterMember(user, requestbody.getFormField());
        return new ResponseEntity<>(new Err(200, ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/getlaporanterkirim/{id}", method = RequestMethod.GET)
    public Map<String, Object> getOneData(@PathVariable("id") Integer id) throws Exception {
//        File files = new File("");
        LaporanKoperasi laporanKoperasi = laporanKoperasiRepository.findById(id).get();
//        FileInputStream file = new FileInputStream(String.format("%s%s", files.getAbsoluteFile(),
//                laporanKoperasi.getPathLaporan()));
        Map<String, Object> data = new HashMap<>();
        DriveQuickstart driveQuickstart = new DriveQuickstart();
        if(!laporanKoperasi.getExtensiFile().equals("pdf")){
            Drive.Files.Export file = driveQuickstart.getFileXLS(laporanKoperasi.getPathLaporan(),laporanKoperasi.getExtensiFile());
            OutputStream outputStream = new ByteArrayOutputStream();
            file.executeMediaAndDownloadTo(outputStream);
            ByteArrayOutputStream bos = (ByteArrayOutputStream) outputStream;
            data.put("file", bos.toByteArray());
        }else {
            OutputStream outputStream = new ByteArrayOutputStream();
            driveQuickstart.getFilePDF(laporanKoperasi.getPathLaporan()).executeMediaAndDownloadTo(outputStream);
            ByteArrayOutputStream bos = (ByteArrayOutputStream) outputStream;
            data.put("file", bos.toByteArray());
        }
        data.put("ext", laporanKoperasi.getExtensiFile());
        System.out.println();
        return data;
    }

    @RequestMapping(value = "/api/changestatuslaporan/{id}", method = RequestMethod.PUT)
    public void getOneData(@PathVariable("id") Integer id, @RequestBody Requestbody requestbody) {
        Integer status = (requestbody.getStatusLaporan().equals("Request") ? 1 : (requestbody.getStatusLaporan().equals("In Review") ? 2 : 3));
        LaporanKoperasi laporanKoperasi = laporanKoperasiRepository.getOne(id);
        laporanKoperasi.setStatus(status);
        laporanKoperasiRepository.save(laporanKoperasi);
    }

    @RequestMapping(value = "/api/downloadlaporankeuangan/{id}", method = RequestMethod.GET)
    public Map<String, Object> downloadLaporanKoperasi(@PathVariable("id") Integer id, HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=pemasukanbarang.xls");
//        File files = new File("");
        LaporanKoperasi laporanKoperasi = laporanKoperasiRepository.findById(id).get();
//        FileInputStream file = new FileInputStream(String.format("%s%s", files.getAbsoluteFile(),
//                laporanKoperasi.getPathLaporan()));
//        IOUtils.copy(file, response.getOutputStream());

        Map<String, Object> data = new HashMap<>();
        DriveQuickstart driveQuickstart = new DriveQuickstart();
        Drive.Files.Export file = driveQuickstart.getFileXLS(laporanKoperasi.getPathLaporan(), laporanKoperasi.getExtensiFile());
        OutputStream outputStream = new ByteArrayOutputStream();
        file.executeMediaAndDownloadTo(outputStream);
        ByteArrayOutputStream bos = (ByteArrayOutputStream) outputStream;
        InputStream bytearr =new ByteArrayInputStream(bos.toByteArray());
        data.put("file", bos.toByteArray());
        data.put("ext", laporanKoperasi.getExtensiFile());
//                IOUtils.write(bos.toByteArray(), response.getOutputStream());
//        IOUtils.copy(bytearr, response.getOutputStream());
        return data;

    }

    @RequestMapping(value = "/api/getlaporanterkirimkoperasi", method = RequestMethod.GET)
    public Map<String, Object> getLaporanTerkirimKoperasi(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Map<String, Object> data = new HashMap<>();
        data.put("datatable", laporanKoperasiRepository.getAllByKoperasi((Integer) koperasi.get("id")));
        return data;
    }

    @RequestMapping(value = "/api/kirimlaporankoperasi", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Err> kirimLaporanKoperasi(HttpServletRequest request, @ModelAttribute Requestbody requestbody) throws Exception {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Boolean exist = laporanKoperasiRepository.existBy((Integer) koperasi.get("id"), requestbody.getTahun());
        if (exist) {
            return new ResponseEntity<>(new Err(400, "Laporan dengan tanggal " + requestbody.getTahun() + " sudah Pernah dilakukan."), HttpStatus.OK);
        }
        koperasiService.buatLaporan((Integer) koperasi.get("id"), requestbody.getTahun(), requestbody.getFiles());
        return new ResponseEntity<>(new Err(200, ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/kirimpesanlaporan/{id}", method = RequestMethod.POST)
    public void kirimLaporanKoperasi(@RequestBody Requestbody requestbody, @PathVariable("id") Integer id) {
        LaporanKoperasi laporanKoperasi = laporanKoperasiRepository.findById(id).get();
        NotifikasiKoperasi notifikasiKoperasi = new NotifikasiKoperasi();
        notifikasiKoperasi.setIdKoperasi(laporanKoperasi.getIdKoperasi());
        notifikasiKoperasi.setIdLaporan(id);
        notifikasiKoperasi.setPesan(requestbody.getText());
        notifikasiKoperasi.setStatus(false);
        notifikasiKoperasi.setTanggalPengiriman(new Date());
        notifikasiKoperasiRepository.save(notifikasiKoperasi);
    }

    @RequestMapping(value = "/api/getlaporankoperasikabtoba", method = RequestMethod.GET)
    public Map<String, Object> getLaporanForAdmin() {
        Map<String, Object> data = new HashMap<>();
        data.put("datatable", laporanKoperasiRepository.getAllLaporan());
        return data;
    }

    @RequestMapping(value = "/api/getnotifikasikoperasi", method = RequestMethod.GET)
    public Map<String, Object> getNotifikasiKoperasi(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Map<String, Object> data = new HashMap<>();
        Boolean exist = notifikasiKoperasiRepository.getExistByKoperasi((Integer) koperasi.get("id"), false);
        data.put("exist", exist);
        if (exist) {
            data.put("total", notifikasiKoperasiRepository.getTotalNotifikasi((Integer) koperasi.get("id")));
        }
        return data;
    }

    @RequestMapping(value = "/api/changestatusnotifkoperasi", method = RequestMethod.GET)
    public void changeState(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        notifikasiKoperasiRepository.ubahStatus((Integer) koperasi.get("id"));
    }

    @RequestMapping(value = "/api/getnotifikasiexistanddata", method = RequestMethod.GET)
    public Map<String, Object> getDataNotifikasiKoperasi(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> koperasi = koperasiRepository.getKoperasiUserId((Integer) user.get("id"));
        Boolean exist = notifikasiKoperasiRepository.getExistByKoperasiKoperasi((Integer) koperasi.get("id"));
        data.put("exist", exist);
        if (exist) {
            data.put("datatable", notifikasiKoperasiRepository.getAllByKoperasi((Integer) koperasi.get("id")));
        }
        return data;
    }

    @RequestMapping(value = "/api/simpaneditlaporan/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public void editLaporanKoperasi(@ModelAttribute Requestbody requestbody, @PathVariable("id") Integer id) throws Exception {
        koperasiService.saveEditLaporan(id, requestbody);
    }
}
