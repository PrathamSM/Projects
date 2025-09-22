package com.jwt.SpringSecurity.service;

import com.jwt.SpringSecurity.model.UserData;
import com.jwt.SpringSecurity.model.UserPrinciple;
import com.jwt.SpringSecurity.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo repo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      UserData user=repo.findByUsername(username);

      if(user==null){
          System.out.println("User Not Found");
throw new UsernameNotFoundException("user not found");
      }
        return new UserPrinciple(user);
    }
}
