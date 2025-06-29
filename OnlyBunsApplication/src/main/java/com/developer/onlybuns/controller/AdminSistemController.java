package com.developer.onlybuns.controller;

import com.developer.onlybuns.dto.AdminSistemDTO;
import com.developer.onlybuns.dto.LoginDTO;
import com.developer.onlybuns.entity.AdminSistem;
import com.developer.onlybuns.entity.Korisnik;
import com.developer.onlybuns.entity.Objava;
import com.developer.onlybuns.rabbitmq.RabbitMQProducerService;
import com.developer.onlybuns.service.AdminSistemService;
import com.developer.onlybuns.service.ObjavaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/adminsistem")
public class AdminSistemController {

    private final AdminSistemService adminSistemService;

    private final RabbitMQProducerService rabbitMQProducerService;

    private final ObjavaService objavaService;

    public AdminSistemController(AdminSistemService adminSistemService, RabbitMQProducerService rabbitMQProducerService, ObjavaService objavaService) {
        this.adminSistemService = adminSistemService;
        this.rabbitMQProducerService = rabbitMQProducerService;
        this.objavaService=objavaService;
    }

    @GetMapping
    public List<AdminSistem> findAll() {
        return adminSistemService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<AdminSistem> findById(@PathVariable("id") Integer id) {
        return adminSistemService.findById(id);
    }

    @PostMapping("/save")
    public AdminSistem saveAdminSistem(@RequestBody AdminSistem adminSistem) {
        return adminSistemService.saveAdminSistem(adminSistem);
    }

    @PostMapping("/updatepassword")
    public ResponseEntity<String> updatePassword(@RequestBody LoginDTO loginDTO) {
        if (adminSistemService.updatePassword(loginDTO)) {
            return ResponseEntity.ok("{\"message\": \"Uspesno ste promenili lozinku.\"}");
        } else {
            return ResponseEntity.status(401).body("Unesite ispravnu lozinku ponovo.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginAdminSistem(@RequestBody LoginDTO loginDTO) {
        AdminSistem adminSistem = adminSistemService.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
        if (adminSistem != null) {
            AdminSistemDTO adminSistemDTO = new AdminSistemDTO();
            adminSistemDTO.setMessage("Uspesna prijava.");
            adminSistemDTO.setIme(adminSistem.getIme());
            adminSistemDTO.setPrezime(adminSistem.getPrezime());
            adminSistemDTO.setEmail(adminSistem.getEmail());

            return ResponseEntity.ok(adminSistemDTO);
        } else {
            return ResponseEntity.status(401).body("Neispravan mejl ili lozinka.");
        }
    }


    @DeleteMapping("/{id}")
    public void deleteAdminSistem(@PathVariable("id") Integer id) {
        adminSistemService.deleteAdminSistem(id);
    }

    @GetMapping("/emails")
    public ResponseEntity<List<String>> getAllEmails() {
        List<String> emails = adminSistemService.getAllEmails();
        return ResponseEntity.ok(emails);
    }

    @PreAuthorize("hasAuthority('KORISNIK')")
    @PostMapping("/posalji-reklamama")
    public ResponseEntity<String> sendPostsToAdvertisers(
            @RequestBody List<Integer> objaveId,
            Authentication authentication
    ) {
        Korisnik korisnik = (Korisnik) authentication.getPrincipal();

        List<Integer> neuspesneObjave = new ArrayList<>();
        boolean nekaUspesnoPoslata = false;

        for (Integer postId : objaveId) {
            Optional<Objava> objavaOpt = objavaService.getById(postId);

            if (objavaOpt.isPresent()) {
                Objava objava = objavaOpt.get();
                try {
                    String porukaReklama = String.format(
                            "Opis: %s, vreme objavljivanja: %s, korisnicko ime: %s",
                            objava.getOpis(),
                            objava.getVremeKreiranja().toString(),
                            objava.getRegistrovaniKorisnik().getKorisnickoIme()
                    );
                    rabbitMQProducerService.posaljiPorukuReklamnimAgencijama(porukaReklama);
                    nekaUspesnoPoslata = true;
                } catch (Exception e) {
                    neuspesneObjave.add(postId);
                }
            } else {
                neuspesneObjave.add(postId);
            }
        }

        if (!nekaUspesnoPoslata) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Nijedna od navedenih objava nije pronadjena i nije mogla biti poslata.");
        }

        if (!neuspesneObjave.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.PARTIAL_CONTENT)
                    .body("Poslate informacije, sem o objavama iz skupa id-jeva: " + neuspesneObjave + ", jer takve objave ne postoje.");
        }

        return ResponseEntity.ok("Uspesno poslate informacije agencijama o selektovanim objavama.");
    }

}
