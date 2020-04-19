package io.github.sandy.controller;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.model.AnggotaKoperasi;
import io.github.sandy.model.Koperasi;
import io.github.sandy.repository.DaftarAnggotaKoperasiRepository;
import io.github.sandy.repository.KoperasiRepository;
import io.github.sandy.repository.UserRepository;
import io.github.sandy.request.Requestbody;
import io.github.sandy.service.KoperasiService;
import io.github.sandy.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

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
    public List<AnggotaKoperasi> getData(HttpServletRequest request) {
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

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
