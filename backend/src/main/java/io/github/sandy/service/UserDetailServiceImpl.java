package io.github.sandy.service;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.config.AuthorizationServerConfiguration;
import io.github.sandy.model.*;
import io.github.sandy.repository.*;
import io.github.sandy.request.Requestbody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    PermissionRepository permissionRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Map<String, Object>> optionalUser = userRepository.getdataFromUname(s);
        optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username or password is Wrong"));
        Map<String, Object> temp = optionalUser.get();
        User user = new User();
        user.setId((Integer) temp.get("id"));
        user.setUsername(s);
        user.setPassword((String) temp.get("password"));
        user.setEmail((String) temp.get("email"));
        user.setEnabled((Boolean) temp.get("enabled"));
        user.setAccountNonExpired((Boolean) temp.get("account_non_expired"));
        user.setCredentialsNonExpired((Boolean) temp.get("credentials_non_expired"));
        user.setAccountNonLocked((Boolean) temp.get("account_non_locked"));
        user.setHaveKoperasi((Integer) temp.get("have_koperasi"));
        List<Map<String, Object>> roleTemp = roleUserRepository.findAllByIdUser((Integer) temp.get("id"));
        List<Role> roleUsers = new ArrayList<>();
        for(Map<String, Object> i : roleTemp){
            Role role =roleRepository.findById((Integer) i.get("role_id")).get();
            roleUsers.add(role);
        }
        user.setRoles(roleUsers);
        UserDetails userDetails = new AuthUserDetail(user);
        new AccountStatusUserDetailsChecker().check(userDetails);
        return userDetails;
    }

    public Err check(Requestbody requestbody) {
        HashMap<String, String> data = new HashMap<>();
        data.put("Username", requestbody.getUsername());
        data.put("Password", requestbody.getPassword());
        data.put("Firstname", requestbody.getFirstName());
        data.put("Lastname", requestbody.getLastName());
        data.put("Alamat", requestbody.getAlamat());
        data.put("Email", requestbody.getEmail());
        data.put("Telepon", requestbody.getTelepon());
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


    public Err userCreate(Requestbody requestbody) {
        User user = new User();
        user.setUsername(requestbody.getUsername());
        user.setPassword(auth.passwordEncoder.encode(requestbody.getPassword()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);
        user.setEmail(requestbody.getEmail());
        userRepository.save(user);

        Role role = roleRepository.findFirstByName("ROLE_koperasi");
        RoleUser roleUser = new RoleUser(role.getId(), user.getId());
        roleUserRepository.save(roleUser);

        UserDetail userDetail = new UserDetail();
        userDetail.setFirstName(requestbody.getFirstName());
        userDetail.setLastName(requestbody.getLastName());
        userDetail.setUser(user);
        userDetail.setAddress(requestbody.getAlamat());
        userDetail.setNoTelepon(requestbody.getTelepon());
        detailUserRepository.save(userDetail);
        return new Err(200, "");
    }

    public void setEnableAcc(int id){
        User user = userRepository.getOne(id);

        MailSender mailSender = new MailSender();
//        mailSender.sendEmailEnableAccount(javaMailSender,user.getEmail());

        user.setEnabled(true);
        userRepository.save(user);
    }
}
