package com.developer.onlybuns.service;

import com.developer.onlybuns.dto.LoginDTO;
import com.developer.onlybuns.entity.AdminSistem;

import java.util.List;
import java.util.Optional;

public interface AdminSistemService {
    Optional<AdminSistem> findById(Integer id);
    List<AdminSistem> findAll();
    AdminSistem saveAdminSistem(AdminSistem adminSistem);
    boolean updatePassword(LoginDTO loginDTO);
    void deleteAdminSistem(Integer id);
    AdminSistem findByEmailAndPassword(String email, String password);
    List<String> getAllEmails();
    AdminSistem proveriKredencijale(String email, String password);


}