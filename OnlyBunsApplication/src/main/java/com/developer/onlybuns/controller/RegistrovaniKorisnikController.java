package com.developer.onlybuns.controller;
import com.developer.onlybuns.dto.KorisnikProfilDTO;
import com.developer.onlybuns.entity.Korisnik;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.entity.UserDetails;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import com.developer.onlybuns.util.TokenUtils;
import org.apache.tomcat.util.json.JSONParser;
import com.developer.onlybuns.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.*;
import org.json.JSONObject;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/registrovaniKorisnik")
public class RegistrovaniKorisnikController {

    private final RegistrovaniKorisnikService registrovaniKorisnikService;

    @Autowired
    private TokenUtils tokenUtils;

    public RegistrovaniKorisnikController(RegistrovaniKorisnikService registrovaniKorisnikService) {
        this.registrovaniKorisnikService = registrovaniKorisnikService;
    }

    @GetMapping
    public List<RegistrovaniKorisnik> findAllRegistrovaniKorisnik() {
        return registrovaniKorisnikService.findAllRegistrovaniKorisnik();
    }

    @GetMapping("/{id}")
    public Optional<RegistrovaniKorisnik> findRegistrovaniKorisnikById(@PathVariable("id") Integer id) {
        return registrovaniKorisnikService.findById(id);
    }

    //prikaz profila za admine koji mogu videti sifre
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

        KorisnikProfilDTO korisnikDTO = new KorisnikProfilDTO(
                korisnik.getKorisnickoIme(),
                korisnik.getIme(),
                korisnik.getPrezime(),
                korisnik.getGrad(),
                korisnik.getDrzava(),
                korisnik.getBroj(),
                korisnik.getEmail(),
                korisnik.getInfo()
        );

        return ResponseEntity.ok(korisnikDTO);
    }


    //prepoznaje securitycontextholder
  /*  @GetMapping("/profil")
    public ResponseEntity<RegistrovaniKorisnik> getUser() {
        String korisnickoIme = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("KORISNIK KOJI GLEDA SVOJ PROFIL JE: " + korisnickoIme);
        RegistrovaniKorisnik user = registrovaniKorisnikService.findByKorisnickoIme(korisnickoIme);

        return ResponseEntity.ok(user);
    }
*/

    //prepoznaje principal-radi u postmanu
    @GetMapping("/profil")
    public ResponseEntity<RegistrovaniKorisnik> getUser(Principal principal) {
        String korisnickoIme = principal.getName();
        System.out.println("KORISNIK KOJI GLEDA SVOJ PROFIL JE: " + korisnickoIme);
        System.out.println("Principal sadrzi: " + principal.getName());
        RegistrovaniKorisnik user = registrovaniKorisnikService.findByKorisnickoIme(korisnickoIme);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/add")
    public ResponseEntity<?> saveRegistrovaniKorisnik(@Valid @RequestBody RegistrovaniKorisnik korisnikEntity) {
        //try catch blok po uzoru na prijaviKorisnika
        try {
            RegistrovaniKorisnik response = registrovaniKorisnikService.saveRegistrovaniKorisnik(korisnikEntity);
            return ResponseEntity.ok(response);

        }  //catch (IllegalArgumentException e) {
        // String jsonString = "{\"Error\":" + "\"" + e.getMessage() + "\"" + "}";
        // JSONObject json = new JSONObject(jsonString);
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(json);
        //  }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            // Vraćanje poruke konflikta sa detaljima
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }

 /*   @PostMapping("/signup")
    public ResponseEntity<RegistrovaniKorisnik> addUser(@RequestBody UserRequest userRequest, UriComponentsBuilder ucBuilder) {
        Optional<RegistrovaniKorisnik> existUserEmail = this.registrovaniKorisnikService.findByEmail(userRequest.getEmail());

        if (existUserEmail.isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        RegistrovaniKorisnik existUserUsername = this.registrovaniKorisnikService.findByKorisnickoIme(userRequest.getKorisnickoIme());

        if (existUserUsername != null) {
            throw new RuntimeException("Username already exists");
        }

        RegistrovaniKorisnik regKorisnik = this.registrovaniKorisnikService.saveRegistrovaniKorisnik(userRequest);

        return new ResponseEntity<>(regKorisnik, HttpStatus.CREATED);
    }
*/







    //pocetni login, bez tokena, za autentifikaciju koristi se login iz authcontrollera
    @PostMapping("/login")
    public ResponseEntity<String> prijaviKorisnika(@RequestBody RegistrovaniKorisnik korisnik) {
        RegistrovaniKorisnik userCredentials = registrovaniKorisnikService.proveriKredencijale(korisnik.getEmail(), korisnik.getPassword());
        if (userCredentials != null) {
            return ResponseEntity.ok("{\"message\": \"Prijava uspesna!\"}");
        } else {
            return ResponseEntity.status(401).body("Neispravan mejl ili lozinka.");
        }
    }

    @PutMapping
    public RegistrovaniKorisnik updateRegistrovaniKorisnik(@RequestBody RegistrovaniKorisnik employeeEntity) {
        return registrovaniKorisnikService.updateRegistrovaniKorisnik(employeeEntity);
    }

    @DeleteMapping("/{id}")
    public void deleteRegistrovaniKorisnik(@PathVariable("id") Integer id) {
        registrovaniKorisnikService.deleteRegistrovaniKorisnik(id);
    }

    @GetMapping("/emails")
    public ResponseEntity<List<String>> getAllEmails() {
        List<String> emails = registrovaniKorisnikService.getAllEmails();
        return ResponseEntity.ok(emails);
    }

}
