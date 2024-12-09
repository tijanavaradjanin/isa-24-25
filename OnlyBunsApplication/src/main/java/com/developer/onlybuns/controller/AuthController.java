package com.developer.onlybuns.controller;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.HashMap;

import com.developer.onlybuns.dto.LoginDTO;
import com.developer.onlybuns.entity.AdminSistem;
import com.developer.onlybuns.entity.Korisnik;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.entity.UserDetails;
import com.developer.onlybuns.service.AdminSistemService;
import com.developer.onlybuns.service.JwtTokenUtil;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/auth"})
public class AuthController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RegistrovaniKorisnikService registrovaniKorisnikService;
    @Autowired
    private AdminSistemService adminSistemService;

    public AuthController() {
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Korisnik korisnik) {
        try {
            RegistrovaniKorisnik registrovaniKorisnik = registrovaniKorisnikService.proveriKredencijale(korisnik.getEmail(), korisnik.getPassword());
            AdminSistem adminSistem  = adminSistemService.proveriKredencijale(korisnik.getEmail(), korisnik.getPassword());

            if(registrovaniKorisnik != null){
                final String token = jwtTokenUtil.generateToken(registrovaniKorisnik);
                HashMap<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("message", "Login successful. Token: " + token);
                return ResponseEntity.ok(response);
            }
            else if(adminSistem != null){
                final String token = jwtTokenUtil.generateToken(adminSistem);
                HashMap<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("message", "Login successful. Token: " + token);
                return ResponseEntity.ok(response);
            }
            else{  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Pogresan email ili sifra"); }

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
        }
    }



}

