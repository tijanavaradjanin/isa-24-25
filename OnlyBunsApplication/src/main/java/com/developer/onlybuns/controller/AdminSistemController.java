package com.developer.onlybuns.controller;

import com.developer.onlybuns.dto.request.AdminSistemDTO;
import com.developer.onlybuns.dto.request.LoginDTO;
import com.developer.onlybuns.entity.AdminSistem;
import com.developer.onlybuns.service.AdminSistemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/adminsistem")
public class AdminSistemController {

    private final AdminSistemService adminSistemService;

    public AdminSistemController(AdminSistemService adminSistemService) {
        this.adminSistemService = adminSistemService;
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
            return ResponseEntity.ok("{\"message\": \"Uspesna promena lozinke.\"}");
        } else {
            return ResponseEntity.status(401).body("Proverite email i lozinku.");
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
            //return ResponseEntity.ok("{\"message\": \"Uspesna prijava.\"}");
        } else {
            return ResponseEntity.status(401).body("Neuspesna prijava. Proverite email i lozinku.");
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

}
