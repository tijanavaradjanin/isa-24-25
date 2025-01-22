package com.developer.onlybuns.controller;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.HashMap;

import com.developer.onlybuns.dto.JwtAuthenticationRequest;
import com.developer.onlybuns.dto.LoginDTO;
import com.developer.onlybuns.dto.UserTokenState;
import com.developer.onlybuns.entity.AdminSistem;
import com.developer.onlybuns.entity.Korisnik;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.entity.UserDetails;
import com.developer.onlybuns.service.AdminSistemService;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import com.developer.onlybuns.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping({"/auth"})
public class AuthController {
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private RegistrovaniKorisnikService registrovaniKorisnikService;
    @Autowired
    private AdminSistemService adminSistemService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public AuthController() {
    }

    /*@PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Korisnik korisnik) {
        try {
            RegistrovaniKorisnik registrovaniKorisnik = registrovaniKorisnikService.proveriKredencijale(korisnik.getEmail(), korisnik.getPassword());
            AdminSistem adminSistem  = adminSistemService.proveriKredencijale(korisnik.getEmail(), korisnik.getPassword());

            if(registrovaniKorisnik != null){
                final String token = tokenUtils.generateToken(registrovaniKorisnik.getKorisnickoIme());
                HashMap<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("message", "Login successful. Token: " + token);
                return ResponseEntity.ok(response);
            }
            else if(adminSistem != null){
                final String token = tokenUtils.generateToken(adminSistem.getKorisnickoIme());
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
     */

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        System.out.println("Prijava: " + authenticationRequest.getEmail());
        System.out.println("Lozinka: " + authenticationRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        System.out.println("Kreiranje Authentication tokena: ");
        System.out.println("Korisničko ime: " + authenticationRequest.getEmail());
        System.out.println("Lozinka: " + authenticationRequest.getPassword());


        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();
        System.out.println("Generisan JWT token: " + jwt);

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

































}


