package io.github.sandy.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
public class AuthenticationController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> register(){
        return new ResponseEntity<>("hello world", HttpStatus.OK);
    }
}
