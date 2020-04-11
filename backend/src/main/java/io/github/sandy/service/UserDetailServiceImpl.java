package io.github.sandy.service;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.config.AuthorizationServerConfiguration;
import io.github.sandy.model.*;
import io.github.sandy.repository.DetailUserRepository;
import io.github.sandy.repository.RoleRepository;
import io.github.sandy.repository.RoleUserRepository;
import io.github.sandy.repository.UserRepository;
import io.github.sandy.request.RequestLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleUserRepository roleUserRepository;

    @Autowired
    AuthorizationServerConfiguration auth;
    @Autowired
    DetailUserRepository detailUserRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(s);
        optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username or password is Wrong"));
        UserDetails userDetails = new AuthUserDetail(optionalUser.get());
        new AccountStatusUserDetailsChecker().check(userDetails);
        return userDetails;
    }

    public Err check(RequestLogin requestLogin) {
        HashMap<String, String> data = new HashMap<>();
        data.put("Username", requestLogin.getUsername());
        data.put("Password", requestLogin.getPassword());
        data.put("Firstname", requestLogin.getFirstName());
        data.put("Lastname", requestLogin.getLastName());
        data.put("Alamat", requestLogin.getAlamat());
        data.put("Email", requestLogin.getEmail());
        data.put("Telepon", requestLogin.getTelepon());
        for (String i : data.keySet()) {
            if (data.get(i).isEmpty()) {
                return new Err(404, i + " Must be Field");
            }
        }
        String email = data.get("Email");
        boolean foundAT = false;
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') {
                foundAT = true;
                break;
            }
        }
        if (!foundAT) {
            return new Err(404, "Email Must be Field with @");
        }
        String telepon = data.get("Telepon");
        boolean foundchar = false;
        for (int i = 0; i < telepon.length(); i++) {
            if (telepon.charAt(i) >= '0' && telepon.charAt(i) <= '9') {
                continue;
            }
            foundchar = true;
            break;
        }
        if (foundchar) {
            return new Err(404, "Telepon Must be Field with number");
        }
        return new Err(200, "");
    }

    public Err searchByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return new Err(403, "Username already exist");
        }
        return new Err(200, "");
    }


    public Err userCreate(RequestLogin requestLogin) {
        User user = new User();
        user.setUsername(requestLogin.getUsername());
        user.setPassword(auth.passwordEncoder.encode(requestLogin.getPassword()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);
        user.setEmail(requestLogin.getEmail());
        userRepository.save(user);

        Role role = roleRepository.findFirstByName("ROLE_koperasi");
        RoleUser roleUser = new RoleUser(role.getId(), user.getId());
        roleUserRepository.save(roleUser);

        UserDetail userDetail = new UserDetail();
        userDetail.setFirstName(requestLogin.getFirstName());
        userDetail.setLastName(requestLogin.getLastName());
        userDetail.setUser(user);
        userDetail.setAddress(requestLogin.getAlamat());
        userDetail.setNoTelepon(requestLogin.getTelepon());
        detailUserRepository.save(userDetail);
        return new Err(200, "");
    }
}
