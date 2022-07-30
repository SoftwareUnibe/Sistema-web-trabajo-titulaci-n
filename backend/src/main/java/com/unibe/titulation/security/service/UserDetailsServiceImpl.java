package com.unibe.titulation.security.service;

import com.unibe.titulation.security.entity.User;
import com.unibe.titulation.security.entity.UserMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String nombreOrEmail) throws UsernameNotFoundException {
        User user = userService.getByUserNameOrEmail(nombreOrEmail)
        .orElseThrow(() ->
        new UsernameNotFoundException("User not found with username or email : " + nombreOrEmail)
        );
        return UserMain.build(user);
    }
    
}
