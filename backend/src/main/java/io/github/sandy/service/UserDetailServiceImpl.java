package io.github.sandy.service;

import io.github.sandy.ErrorCode.Err;
import io.github.sandy.config.AuthorizationServerConfiguration;
import io.github.sandy.model.AuthUserDetail;
import io.github.sandy.model.User;
import io.github.sandy.model.UserDetail;
import io.github.sandy.repository.DetailUserRepository;
import io.github.sandy.repository.UserRepository;
import io.github.sandy.request.RequestLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
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
        for (String i : data.keySet()) {
            if (data.get(i).isEmpty()) {
                return new Err(404, i + " Must be Field");
            }
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


    public Err userCreate(RequestLogin requestLogin){
        User user = new User();
        user.setUsername(requestLogin.getUsername());
        user.setPassword(auth.passwordEncoder.encode(requestLogin.getPassword()));
        userRepository.save(user);

        UserDetail userDetail =  new UserDetail();
        userDetail.setFirstName(requestLogin.getFirstName());
        userDetail.setLastName(requestLogin.getLastName());
        userDetail.setUser(user);
        detailUserRepository.save(userDetail);


        return new Err(200,"");
    }
}
