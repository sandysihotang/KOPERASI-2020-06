package io.github.sandy.controller;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.model.FieldDaftarAnggota;
import io.github.sandy.model.User;
import io.github.sandy.model.UserDetail;
import io.github.sandy.repository.DaftarAnggotaKoperasiRepository;
import io.github.sandy.repository.DetailUserRepository;
import io.github.sandy.repository.UserRepository;
import io.github.sandy.request.Requestbody;
import io.github.sandy.service.KoperasiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
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
}
