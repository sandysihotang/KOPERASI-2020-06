package io.github.sandy.controller;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.config.AuthorizationServerConfiguration;
import io.github.sandy.model.ResetPassword;
import io.github.sandy.model.User;
import io.github.sandy.model.UserDetail;
import io.github.sandy.repository.*;
import io.github.sandy.request.Requestbody;
import io.github.sandy.service.MailSender;
import io.github.sandy.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;


@RestController
public class AuthenticationController {
    @Autowired
    UserRepository userDetailRepository;

    @Autowired
    AuthorizationServerConfiguration auth;

    @Autowired
    UserDetailServiceImpl userDetailService;


    @Autowired
    UserRepository userRepository;

    @Autowired
    KoperasiRepository koperasiRepository;

    @Autowired
    DetailUserRepository detailUserRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ResetPasswordRepository resetPasswordRepository;


    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public void send(HttpServletRequest request) {
        MailSender mailSender = new MailSender();
//        mailSender.run(javaMailSender);
    }

    private UUID getUUID() throws UnsupportedEncodingException {
        System.out.println(LocalDateTime.now().toString());
        String source = LocalDateTime.now().toString();
        byte[] bytes = source.getBytes("UTF-8");
        UUID uuid = type5UUIDFromBytes(bytes);
        return uuid;
    }

    private static UUID constructType5UUID(byte[] data) {
        long msb = 0;
        long lsb = 0;
        assert data.length == 16 : "data must be 16 bytes in length";

        for (int i = 0; i < 8; i++)
            msb = (msb << 8) | (data[i] & 0xff);

        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (data[i] & 0xff);
        return new UUID(msb, lsb);
    }

    public static UUID type5UUIDFromBytes(byte[] name) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("SHA-1 not supported", nsae);
        }
        byte[] bytes = Arrays.copyOfRange(md.digest(name), 0, 16);
        bytes[6] &= 0x0f; /* clear version        */
        bytes[6] |= 0x50; /* set to version 5     */
        bytes[8] &= 0x3f; /* clear variant        */
        bytes[8] |= 0x80; /* set to IETF variant  */
        return constructType5UUID(bytes);
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
        if (user.size() == 0) {
            data.put("exist", false);
            data.put("error", "Username atau Password yang anda masukkan salah");
        } else if (!(Boolean) user.get("enabled")) {
            if ((Integer) user.get("have_koperasi") == 0) {
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

    @RequestMapping(value = "/resetpassword", method = RequestMethod.POST)
    public Map<String, Object> resetPassword(@RequestBody Requestbody request, HttpServletRequest requests) throws Exception {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> user = userRepository.getUserUsername(request.getUsername());
        if (user.size() == 0) {
            data.put("exist", false);
            data.put("error", "Username tidak ditemukan dalam sistem");
        } else if (!(Boolean) user.get("enabled")) {
            if ((Integer) user.get("have_koperasi") == 0) {
                data.put("exist", false);
                data.put("error", "Akun anda telah dinonaktifkan oleh Koperasi.");
            } else {
                data.put("exist", false);
                data.put("error", "Silahkan menunggu aktivasi dari DISKOPERINDAG");
            }
        } else {
            UUID unique_id = getUUID();
            MailSender mailSender = new MailSender();
            String baseUrl = String.format("%s://%s:%d/", requests.getScheme(), requests.getServerName(), requests.getServerPort());
            baseUrl += "#/gantipassword/" + unique_id;
            mailSender.sendEmailResetPassword((String) user.get("email"), baseUrl);
            ResetPassword resetPassword = new ResetPassword();
            resetPassword.setIdUser((Integer) user.get("id"));
            resetPassword.setStatus(false);
            resetPassword.setUniqueId(unique_id.toString());
            resetPasswordRepository.save(resetPassword);
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

    @RequestMapping(value = "/ubahpassword/{uuid}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ubahPassword(@RequestBody Requestbody requestbody, @PathVariable("uuid") String uuid) {
        Boolean exist = resetPasswordRepository.existsByUniquId(uuid);
        Map<String, Object> data = new HashMap<>();
        data.put("exist", exist);
        if (exist) {
            ResetPassword resPassword = resetPasswordRepository.findFirstByUniqueId(uuid);
            if (resPassword.getStatus()) {
                data.put("exist", false);
                data.put("error", "Password sudah pernah diganti sebelumnya");
            } else {
                userRepository.updateUser(resPassword.getIdUser(), auth.passwordEncoder.encode(requestbody.getPassword()));
                ResetPassword up = resetPasswordRepository.getOne(resPassword.getId());
                up.setStatus(true);
                resetPasswordRepository.save(up);
            }
        } else {
            data.put("error", "User tidak ditemukan");
        }
        return data;
    }

    @RequestMapping(value = "/api/terimaacc", method = RequestMethod.POST)
    public void terima(@RequestBody Requestbody requestbody) throws GeneralSecurityException, IOException, MessagingException {
        userDetailService.setEnableAcc(requestbody.getId());
    }
}
