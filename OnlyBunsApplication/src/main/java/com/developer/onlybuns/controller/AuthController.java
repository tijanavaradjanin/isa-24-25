package com.developer.onlybuns.controller;//

import com.developer.onlybuns.dto.JwtAuthenticationRequest;
import com.developer.onlybuns.dto.UserTokenState;
import com.developer.onlybuns.entity.Korisnik;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.service.AdminSistemService;
import com.developer.onlybuns.service.LoginAttemptService;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import com.developer.onlybuns.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

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
    @Autowired
    private LoginAttemptService loginAttemptService;

    public AuthController() {
    }

    //401 unauthorized-ne postoji validan token (nije stavljen ili je pogresan)
    //403 forbidden-postoji validan token ali uloga korisnika nije odgovarajuca za taj zahtev

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletRequest request) {
        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        String ipAddress = request.getRemoteAddr();
        //Provera da li je IP blokiran
        if (loginAttemptService.isBlocked(ipAddress)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Previse neuspesnih pokusaja. Pokusajte ponovo kasnije.");
        }
        try {
            System.out.println("Prijava: " + authenticationRequest.getEmail());
            System.out.println("Lozinka: " + authenticationRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));

            System.out.println("Kreiranje Authentication tokena: ");
            System.out.println("Email: " + authenticationRequest.getEmail());
            System.out.println("Lozinka: " + authenticationRequest.getPassword());

            // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
            // kontekst
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getCredentials());
            // Kreiraj token za tog korisnika
            Korisnik user = (Korisnik) authentication.getPrincipal();
            System.out.println("USER: "+user.getKorisnickoIme());   //ispise korisnicko ime
            String jwt = tokenUtils.generateToken(user.getKorisnickoIme());
            System.out.println(user.getUsername()); //prazno
            int expiresIn = tokenUtils.getExpiredIn();
            System.out.println("Generisan JWT token: " + jwt);

            // Vrati token kao odgovor na uspesnu autentifikaciju
            updateLastLogin(user.getKorisnickoIme());
            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
        } catch (BadCredentialsException e) {
            loginAttemptService.recordAttempt(ipAddress);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Neispravni kredencijali. Pokusajte ponovo.");
        }
    }

    public void updateLastLogin(String korisnickoIme) {
        RegistrovaniKorisnik korisnikOpt = registrovaniKorisnikService.findByKorisnickoIme(korisnickoIme);
        if (korisnikOpt!=null) {
            korisnikOpt.setLastLogin(new Timestamp(System.currentTimeMillis()));
            registrovaniKorisnikService.updateRegistrovaniKorisnik(korisnikOpt);
        }
    }

}


