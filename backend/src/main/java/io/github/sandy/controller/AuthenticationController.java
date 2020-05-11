package io.github.sandy.controller;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.model.User;
import io.github.sandy.repository.KoperasiRepository;
import io.github.sandy.repository.UserRepository;
import io.github.sandy.request.Requestbody;
import io.github.sandy.service.MailSender;
import io.github.sandy.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;


@RestController
public class AuthenticationController {
    @Autowired
    UserRepository userDetailRepository;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    UserRepository userRepository;

    @Autowired
    KoperasiRepository koperasiRepository;

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public void send() {
        MailSender mailSender = new MailSender();
        mailSender.run(javaMailSender);
    }

    @RequestMapping(value = "/api/getnonauthenticateduser", method = RequestMethod.GET)
    public List<User> get() {
        return userDetailRepository.findByRole();
    }

    @RequestMapping(value = "/api/jeniskoperasi", method = RequestMethod.GET)
    public Integer getJenisKoperasi(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        User user = userRepository.findByUsername(uname).get();
        return koperasiRepository.getJenisFromKoperasi(user.getId());
    }

    @RequestMapping(value = "/api/login/currentuser", method = RequestMethod.GET)
    public Principal hello(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return principal;
    }

    @RequestMapping(value = "/api/currentuser", method = RequestMethod.GET)
    public User getUser(HttpServletRequest request) {
        User user = userDetailRepository.findByUsername(request.getUserPrincipal().getName()).get();
        return user;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Err> register(@RequestBody Requestbody requestbody) {
        Err data = userDetailService.check(requestbody);
        if (data.getErrCode() == 404) {
            return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
        }
        data = userDetailService.searchByUsername(requestbody.getUsername());
        if (data.getErrCode() == 403) {
            return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
        }
        data = userDetailService.userCreate(requestbody);
        return new ResponseEntity<>(new Err(200, ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/terimaacc", method = RequestMethod.POST)
    public void terima(@RequestBody Requestbody requestbody) {
        userDetailService.setEnableAcc(requestbody.getId());
    }
}
