package com.developer.onlybuns.service;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import java.util.ArrayList;

import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private RegistrovaniKorisnikService registrovaniKorisnikService;

    public CustomUserDetailsService() {
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RegistrovaniKorisnik registrovaniKorisnik = this.registrovaniKorisnikService.proveriKredencijale(username, "");
        if (registrovaniKorisnik == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        } else {
            return new User(registrovaniKorisnik.getEmail(), registrovaniKorisnik.getPassword(), new ArrayList());
        }
    }
}

