package io.github.sandy.controller;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.model.Pinjaman;
import io.github.sandy.model.User;
import io.github.sandy.repository.PinjamanRepository;
import io.github.sandy.repository.UserRepository;
import io.github.sandy.request.Requestbody;
import io.github.sandy.service.PeminjamanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
public class PeminjamanController {
    @Autowired
    PinjamanRepository pinjamanRepository;

    @Autowired
    PeminjamanService peminjamanService;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/api/requestpeminjaman", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Err> requestPinjaman(@RequestBody Requestbody requestbody, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        peminjamanService.requestPinjaman(user, requestbody);
        return new ResponseEntity<>(new Err(200, ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/getadapinjaman", method = RequestMethod.GET)
    public boolean getAdaPinjaman(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        return pinjamanRepository.existsByUserAndStatusNot(user, 1);
    }

    @RequestMapping(value = "/api/getdatapinjaman", method = RequestMethod.GET)
    public Pinjaman getDataPinjaman(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        return pinjamanRepository.getFirstByUserAndStatusNot(user, 1);
    }

    @RequestMapping(value = "/api/getdatapengajuanpinjaman", method = RequestMethod.GET)
    public List<Pinjaman> getDataRequestPinjaman(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        return pinjamanRepository.getAllByKoperasi(user.getKoperasi());
    }
}
