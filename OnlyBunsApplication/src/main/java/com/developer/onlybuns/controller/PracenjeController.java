package com.developer.onlybuns.controller;

import com.developer.onlybuns.dto.KorisnikProfilDTO;
import com.developer.onlybuns.entity.Pracenje;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.service.PracenjeService;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @PostMapping("/add")
    public ResponseEntity<?> addPracenje(@RequestParam("zapraceni")  String zapraceni, Principal principal) {
        RegistrovaniKorisnik korisnik = registrovaniKorisnikService.findByKorisnickoIme(principal.getName());
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

    @DeleteMapping("/delete")
    public ResponseEntity<?> removePracenje(@RequestParam("zapraceni")  String zapraceni, Principal principal) {
        RegistrovaniKorisnik korisnik = registrovaniKorisnikService.findByKorisnickoIme(principal.getName());
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

    @GetMapping("/provera")
    public ResponseEntity<Map<String, Boolean>> proveraPracenja(@RequestParam("zapraceni") String zapraceni, Principal principal) {
        RegistrovaniKorisnik korisnik = registrovaniKorisnikService.findByKorisnickoIme(principal.getName());
        RegistrovaniKorisnik zapraceniKorisnik = registrovaniKorisnikService.findByKorisnickoIme(zapraceni);

        if (zapraceniKorisnik == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", true));
        }

        boolean prati = pracenjeService.proveriDaLiPrati(korisnik.getId(), zapraceniKorisnik.getId());
        return ResponseEntity.ok(Map.of("prati", prati));
    }
    
    @GetMapping("/listaZapracenih")
    public ResponseEntity<List<KorisnikProfilDTO>> seeFollowingList(Principal principal) {
        RegistrovaniKorisnik ulogovaniKorisnik = registrovaniKorisnikService.findByKorisnickoIme(principal.getName());
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
