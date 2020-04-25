package io.github.sandy.controller;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.model.*;
import io.github.sandy.repository.*;
import io.github.sandy.request.Requestbody;
import io.github.sandy.service.KoperasiService;
import io.github.sandy.service.UserDetailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    AngotaKoperasiRepository angotaKoperasiRepository;

    @Autowired
    KoperasiPengaturanPinjamanRepository koperasiPengaturanPinjamanRepository;

    @Autowired
    PengaturanPinjamanRepository pengaturanPinjamanRepository;

    @RequestMapping(value = "/api/createkoperasi", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Err> createKoperasi(@ModelAttribute Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        koperasiService.createKoperasi(requestbody, user);

        return new ResponseEntity<>(new Err(200, ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/getallkoperasi", method = RequestMethod.GET)
    public List<Koperasi> get() {
        return koperasiRepository.findByIsHaveKoperasi();
    }

    @RequestMapping(value = "/api/changestatekoperasi", method = RequestMethod.POST)
    public ResponseEntity<Err> changeState(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        koperasiService.changeStateKoperasi(requestbody.getId(), requestbody.isState());
        return new ResponseEntity<>(new Err(200, ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/isHaveField", method = RequestMethod.GET)
    public boolean checkIshaveFieldMember(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        Koperasi koperasi = koperasiRepository.getOne(userRepository.findByUsername(user).get().getKoperasi().getId());

        return koperasi.isHaveFieldRegisterMember();
    }


    @RequestMapping(value = "/api/getcolumnmember", method = RequestMethod.GET)
    public String getcolumn(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        Koperasi koperasi = koperasiRepository.getOne(userRepository.findByUsername(user).get().getKoperasi().getId());
        String pattern = daftarAnggotaKoperasiRepository.findByKoperasiId(koperasi.getId()).get().getPatternField();
        return pattern;
    }

    @RequestMapping(value = "/api/getdatamember", method = RequestMethod.GET)
    public Set<AnggotaKoperasi> getData(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        Koperasi koperasi = koperasiRepository.getOne(userRepository.findByUsername(user).get().getKoperasi().getId());
        return koperasi.getAnggotaKoperasis();
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
    public String getStateKoperasi(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();
        return koperasiRepository.findFirstByUser(user).getNamaKoperasi();
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
    public String getNameMember(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        return user.getUserDetail().getFirstName() + " " + user.getUserDetail().getLastName();
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

    @RequestMapping(value = "/api/savepengaturanpinjaman", method = RequestMethod.POST)
    public void savePengaturanPeminjaman(HttpServletRequest request, @RequestBody Requestbody requestbody) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        Koperasi koperasi = koperasiRepository.findFirstByUser(user);
        koperasiService.savePengaturanPeminjaman(koperasi, requestbody);
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}

//@Configuration
//@EnableScheduling
//@ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true)
//class Schedule {
//    private static final Logger log = LoggerFactory.getLogger(Schedule.class);
//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//    @Scheduled(fixedRate = 5000L)
//    public void reportCurruntTime(){
//        log.info("the time is now {}" + dateFormat.format(new Date()));
//    }
//}
