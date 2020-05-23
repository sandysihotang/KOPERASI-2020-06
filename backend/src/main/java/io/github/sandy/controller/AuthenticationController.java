package io.github.sandy.controller;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.model.User;
import io.github.sandy.model.UserDetail;
import io.github.sandy.repository.DetailUserRepository;
import io.github.sandy.repository.KoperasiRepository;
import io.github.sandy.repository.RoleRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @Autowired
    DetailUserRepository detailUserRepository;

    @Autowired
    RoleRepository roleRepository;

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public void send() {
        MailSender mailSender = new MailSender();
//        mailSender.run(javaMailSender);
    }

    @RequestMapping(value = "/api/getnonauthenticateduser", method = RequestMethod.GET)
    public List<Map<String, Object>> get() {
        return userDetailRepository.findByRole();
    }

    @RequestMapping(value = "/api/jeniskoperasi", method = RequestMethod.GET)
    public Integer getJenisKoperasi(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        return koperasiRepository.getJenisFromKoperasi((Integer) user.get("id"));
    }

    @RequestMapping(value = "/api/login/currentuser", method = RequestMethod.GET)
    public Map<String, Object> hello(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> userDetail = detailUserRepository.findUserDetail((Integer) user.get("id"));
        data.put("userDetail", userDetail);
        data.put("haveKoperasi", user.get("have_koperasi"));
        Map<String, Object> role = roleRepository.find((Integer) user.get("id"));
        data.put("name", role.get("name"));
        return data;
    }

    @RequestMapping(value = "/checkcredential", method = RequestMethod.POST)
    public Map<String, Object> hello(@RequestBody Requestbody request) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> user = userRepository.getUserUsername(request.getUsername());
        if(user.isEmpty()){
            data.put("exist", false);
            data.put("error", "Username atau Password yang anda masukkan salah");
        }

        if(!(Boolean) user.get("enabled")){
            if((Integer) user.get("have_koperasi") == 0 ){
                data.put("exist", false);
                data.put("error", "Akun anda telah dinonaktifkan oleh Koperasi.");
            } else {
                data.put("exist", false);
                data.put("error", "Silahkan menunggu aktivasi dari DISKOPERINDAG");
            }
        } else {
            data.put("exist", true);
        }
        return data;
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
