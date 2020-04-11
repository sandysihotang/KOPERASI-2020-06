package io.github.sandy.controller;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.model.Koperasi;
import io.github.sandy.model.User;
import io.github.sandy.repository.KoperasiRepository;
import io.github.sandy.repository.UserRepository;
import io.github.sandy.request.Requestbody;
import io.github.sandy.service.KoperasiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    UserRepository userRepository;

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

    @RequestMapping(value = "/api/changestatekoperasi" , method = RequestMethod.POST)
    public ResponseEntity<Err> changeState(@RequestBody Requestbody requestbody, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        String user = principal.getName();
        koperasiService.changeStateKoperasi(requestbody.getId(), requestbody.isState());
        return new ResponseEntity<>(new Err(200, ""), HttpStatus.OK);
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
