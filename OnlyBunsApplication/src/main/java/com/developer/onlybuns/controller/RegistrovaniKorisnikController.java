package com.developer.onlybuns.controller;
import com.developer.onlybuns.entity.Korisnik;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.entity.UserDetails;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import com.developer.onlybuns.util.TokenUtils;
import org.apache.tomcat.util.json.JSONParser;
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
    public ResponseEntity<?> saveRegistrovaniKorisnik(@RequestBody RegistrovaniKorisnik employeeEntity) {
        //try catch blok po uzoru na prijaviKorisnika
        try {
            RegistrovaniKorisnik response=registrovaniKorisnikService.saveRegistrovaniKorisnik(employeeEntity);
            return ResponseEntity.ok(response);

        }  catch (IllegalArgumentException e) {
            String jsonString = "{\"Error\":" + "\"" + e.getMessage() + "\"" + "}";
            JSONObject json = new JSONObject(jsonString);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(json);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate entry for username or email");
        }
    }

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
