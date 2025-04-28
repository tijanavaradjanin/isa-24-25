package com.developer.onlybuns.controller;

import com.developer.onlybuns.dto.KorisnikProfilDTO;
import com.developer.onlybuns.entity.Pracenje;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.service.PracenjeService;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/pracenje")
public class PracenjeController {

    private final PracenjeService pracenjeService;

    private final RegistrovaniKorisnikService registrovaniKorisnikService;

    public PracenjeController(PracenjeService pracenjeService, RegistrovaniKorisnikService registrovaniKorisnikService) {
        this.pracenjeService = pracenjeService;
        this.registrovaniKorisnikService=registrovaniKorisnikService;
    }

    @PreAuthorize("hasAuthority('KORISNIK')")
    @PostMapping("/add")
    public ResponseEntity<?> addPracenje(@RequestParam("zapraceni")  String zapraceni, Authentication authentication) {
        RegistrovaniKorisnik korisnik = (RegistrovaniKorisnik) authentication.getPrincipal();
        RegistrovaniKorisnik zapraceniKorisnik=registrovaniKorisnikService.findByKorisnickoIme(zapraceni);
        if(zapraceniKorisnik==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Korisnik sa tim korisnickim imenom nije pronadjen.");
        } else {
            Pracenje pracenje = new Pracenje(korisnik, zapraceniKorisnik, LocalDateTime.now());
              if(pracenjeService.proveriDaLiPrati(korisnik.getId(), zapraceniKorisnik.getId())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Vec pratite ovog korisnika.");
              } else {
                pracenjeService.savePracenje(pracenje);
                return ResponseEntity.ok("Korisnik uspesno zapracen!");
              }
        }
    }

    @PreAuthorize("hasAuthority('KORISNIK')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> removePracenje(@RequestParam("zapraceni")  String zapraceni, Authentication authentication) {
        RegistrovaniKorisnik korisnik = (RegistrovaniKorisnik) authentication.getPrincipal();
        RegistrovaniKorisnik zapraceniKorisnik=registrovaniKorisnikService.findByKorisnickoIme(zapraceni);
        if(zapraceniKorisnik==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Korisnik sa tim korisnickim imenom nije pronadjen.");
        } else {
            if (pracenjeService.pronadjiPracenje(korisnik.getId(), zapraceniKorisnik.getId()) == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Ne mozete otpratiti korisnika, jer ga ne pratite.");
            } else {
                pracenjeService.removePracenje(pracenjeService.pronadjiPracenje(korisnik.getId(), zapraceniKorisnik.getId()));
                return ResponseEntity.ok("Uspesno ste otpratili korisnika!");
            }
        }
    }

    @PreAuthorize("hasAuthority('KORISNIK')")
    @GetMapping("/provera")
    public ResponseEntity<Map<String, Boolean>> proveraPracenja(@RequestParam("zapraceni") String zapraceni, Authentication authentication) {
        RegistrovaniKorisnik korisnik = (RegistrovaniKorisnik) authentication.getPrincipal();
        RegistrovaniKorisnik zapraceniKorisnik = registrovaniKorisnikService.findByKorisnickoIme(zapraceni);

        if (zapraceniKorisnik == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", true));
        }

        boolean prati = pracenjeService.proveriDaLiPrati(korisnik.getId(), zapraceniKorisnik.getId());
        return ResponseEntity.ok(Map.of("prati", prati));
    }

    @PreAuthorize("hasAuthority('KORISNIK')")
    @GetMapping("/listaZapracenih")
    public ResponseEntity<List<KorisnikProfilDTO>> seeFollowingList(Authentication authentication) {
        RegistrovaniKorisnik ulogovaniKorisnik = (RegistrovaniKorisnik) authentication.getPrincipal();
        List<RegistrovaniKorisnik> zapraceniKorisnici= pracenjeService.zapraceniKorisnici(ulogovaniKorisnik.getId());
        List<KorisnikProfilDTO> zapraceniKorisniciPrikaz=new ArrayList<>();
        for(RegistrovaniKorisnik korisnik: zapraceniKorisnici){
            zapraceniKorisniciPrikaz.add(new KorisnikProfilDTO(
                    korisnik.getKorisnickoIme(),
                    korisnik.getIme(),
                    korisnik.getPrezime(),
                    korisnik.getGrad(),
                    korisnik.getDrzava(),
                    korisnik.getBroj(),
                    korisnik.getEmail(),
                    korisnik.getInfo()
                    )
            );
        }
        return ResponseEntity.ok(zapraceniKorisniciPrikaz);
    }









}
