package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.dto.LoginDTO;
import com.developer.onlybuns.entity.AdminSistem;
import com.developer.onlybuns.entity.Uloga;
import com.developer.onlybuns.repository.AdminSistemRepository;
import com.developer.onlybuns.service.AdminSistemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminSistemServiceImpl implements AdminSistemService {
    @Autowired
    private final AdminSistemRepository adminSistemRepository;

    public AdminSistemServiceImpl(AdminSistemRepository adminSistemRepository) {
        this.adminSistemRepository = adminSistemRepository;
    }

    public Optional<AdminSistem> findById(Integer id) {
        return adminSistemRepository.findById(id);
    }

    public List<AdminSistem> findAll() {
        return adminSistemRepository.findAll();
    }

    public AdminSistem saveAdminSistem(AdminSistem adminSistem) {
        adminSistem.setUloga(Uloga.ADMIN);
        return adminSistemRepository.save(adminSistem);
    }

    public AdminSistem findByEmailAndPassword(String email, String password) {
        return adminSistemRepository.findByEmailAndPassword(email, password);
    }
    public void deleteAdminSistem(Integer id) {
        adminSistemRepository.deleteById(id);
    }

    public boolean updatePassword(LoginDTO loginDTO) {
        AdminSistem adminSistem = adminSistemRepository.findByEmail(loginDTO.getEmail());

        if (adminSistem != null) {
            adminSistem.setPassword(loginDTO.getPassword());
            adminSistemRepository.save(adminSistem);
            return true;
        }
        return false;
    }

    @Override
    public List<String> getAllEmails() {
        return adminSistemRepository.findAllEmails();
    }

    public AdminSistem proveriKredencijale(String email, String password) {
        AdminSistem korisnik = this.adminSistemRepository.findByEmailAndPassword(email, password);
        return korisnik != null ? korisnik : null;
    }



}
