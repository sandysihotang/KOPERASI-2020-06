package io.github.sandy.controller;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.repository.UserRepository;
import io.github.sandy.request.RequestLogin;
import io.github.sandy.service.MailSender;
import io.github.sandy.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@RestController
public class AuthenticationController {
    @Autowired
    UserRepository userDetailRepository;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    JavaMailSender javaMailSender;

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public void send(){
        MailSender mailSender = new MailSender();
        mailSender.run(javaMailSender);
    }



    @RequestMapping(value = "/api/login/currentuser", method = RequestMethod.GET)
    public Principal hello(HttpServletRequest request){
        Principal principal =request.getUserPrincipal();
        return principal;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Err> register(@RequestBody RequestLogin requestLogin) {
        Err data = userDetailService.check(requestLogin);
        if (data.getErrCode() == 404) {
            return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
        }
        data  = userDetailService.searchByUsername(requestLogin.getUsername());
        if (data.getErrCode() == 403){
            return new ResponseEntity<>(data,HttpStatus.FORBIDDEN);
        }
        data = userDetailService.userCreate(requestLogin);
        return new ResponseEntity<>(new Err(200,""),HttpStatus.OK);
    }
}
