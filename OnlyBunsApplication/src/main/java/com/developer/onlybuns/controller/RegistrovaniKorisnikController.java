package com.developer.onlybuns.controller;
import com.developer.onlybuns.dto.KorisnikProfilDTO;
import com.developer.onlybuns.dto.UserRequest;
import com.developer.onlybuns.entity.Lokacija;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.service.LokacijaService;
import com.developer.onlybuns.service.ObjavaService;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import com.developer.onlybuns.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/registrovaniKorisnik")
public class RegistrovaniKorisnikController {

    private final RegistrovaniKorisnikService registrovaniKorisnikService;

    private final ObjavaService objavaService;

    private final LokacijaService lokacijaService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TokenUtils tokenUtils;

    public RegistrovaniKorisnikController(RegistrovaniKorisnikService registrovaniKorisnikService, ObjavaService objavaService, LokacijaService lokacijaService) {
        this.registrovaniKorisnikService = registrovaniKorisnikService;
        this.objavaService = objavaService;
        this.lokacijaService=lokacijaService;
    }

    //prikaz profila za admine
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/korisnickoIme/{korisnickoIme}")
    public RegistrovaniKorisnik findRegistrovaniKorisnikByKorisnickoIme(@PathVariable("korisnickoIme") String korisnickoIme) {
        return registrovaniKorisnikService.findByKorisnickoIme(korisnickoIme);
    }

    //prikaz profila za neautentifikovane korisnike
    @GetMapping("/korisnikProfil/{korisnickoIme}")
    public ResponseEntity<KorisnikProfilDTO> findKorisnikProfilByKorisnickoIme(
            @PathVariable("korisnickoIme") String korisnickoIme) {

        RegistrovaniKorisnik korisnik = registrovaniKorisnikService.findByKorisnickoIme(korisnickoIme);

        if (korisnik == null) {
            return ResponseEntity.notFound().build();
        }

        String adresaLokacije=lokacijaService.getAdresa(korisnik.getLokacija().getLatituda(), korisnik.getLokacija().getLongituda());

        KorisnikProfilDTO korisnikDTO = new KorisnikProfilDTO(
                korisnik.getKorisnickoIme(),
                korisnik.getIme(),
                korisnik.getPrezime(),
                adresaLokacije,
                korisnik.getBroj(),
                korisnik.getEmail(),
                korisnik.getInfo()
        );

        return ResponseEntity.ok(korisnikDTO);
    }

    @PreAuthorize("hasAnyAuthority('KORISNIK', 'ADMIN')")
    @GetMapping("/profil")
    public ResponseEntity<KorisnikProfilDTO> getUser(Authentication authentication) {
            RegistrovaniKorisnik user = (RegistrovaniKorisnik) authentication.getPrincipal();
            String adresa=lokacijaService.getAdresa(user.getLokacija().getLatituda(), user.getLokacija().getLongituda());
            KorisnikProfilDTO korisnik=new KorisnikProfilDTO(
                    user.getKorisnickoIme(),
                    user.getIme(),
                    user.getPrezime(),
                    adresa,
                    user.getBroj(),
                    user.getEmail(),
                    user.getInfo()
            );
            return ResponseEntity.ok(korisnik);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserRequest userRequest, BindingResult result) {
        System.out.println(userRequest.getDrzava() + userRequest.getPrezime() + userRequest.getInfo());
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
        } else {
            Optional<RegistrovaniKorisnik> existUserEmail = this.registrovaniKorisnikService.findByEmail(userRequest.getEmail());
            if (existUserEmail.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Profil sa tim mejlom vec postoji.");
            } else {
                RegistrovaniKorisnik existUserUsername = this.registrovaniKorisnikService.findByKorisnickoIme(userRequest.getKorisnickoIme());
                if (existUserUsername != null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Korisnicko ime je zauzeto.");
                } else {
                    if (!Objects.equals(userRequest.getPassword(), userRequest.getPotvrdaLozinke())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lozinka i njena potvrda se moraju poklapati.");
                    } else {
                        if (objavaService.validateLocation(userRequest.getGrad(), userRequest.getDrzava(), userRequest.getUlica(), userRequest.getBrojKuce()) == null) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nevalidna lokacija.");
                        } else {
                            double[] koordinate = objavaService.validateLocation(userRequest.getGrad(), userRequest.getDrzava(), userRequest.getUlica(), userRequest.getBrojKuce());
                            Lokacija lokacija = new Lokacija();
                            lokacija.setLatituda(koordinate[0]);
                            lokacija.setLongituda(koordinate[1]);
                            lokacijaService.saveLokacija(lokacija);
                            RegistrovaniKorisnik regKorisnik = new RegistrovaniKorisnik(
                                    userRequest.getEmail(),
                                    userRequest.getPassword(),
                                    userRequest.getIme(),
                                    userRequest.getPrezime(),
                                    userRequest.getKorisnickoIme(),
                                    lokacija,
                                    userRequest.getBroj(),
                                    userRequest.getInfo()
                            );
                            try {
                                registrovaniKorisnikService.saveRegistrovaniKorisnik(regKorisnik);
                                return new ResponseEntity<>(regKorisnik, HttpStatus.CREATED);
                            } catch (DataIntegrityViolationException ex) {
                                return ResponseEntity.status(HttpStatus.CONFLICT).body("Korisničko ime ili email su već zauzeti.");
                            }
                        }
                    }
                }
            }
        }
    }

    //test get metode
    @GetMapping("/emails")
    public ResponseEntity<List<String>> getAllEmails() {
        List<String> emails = registrovaniKorisnikService.getAllEmails();
        return ResponseEntity.ok(emails);
    }
}
