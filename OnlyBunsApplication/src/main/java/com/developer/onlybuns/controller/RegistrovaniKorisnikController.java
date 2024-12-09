package com.developer.onlybuns.controller;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/registrovaniKorisnik")
public class RegistrovaniKorisnikController {

    private final RegistrovaniKorisnikService registrovaniKorisnikService;

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

    @PostMapping("/add")
    public RegistrovaniKorisnik saveRegistrovaniKorisnik(@RequestBody RegistrovaniKorisnik employeeEntity) {
        return registrovaniKorisnikService.saveRegistrovaniKorisnik(employeeEntity);
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
