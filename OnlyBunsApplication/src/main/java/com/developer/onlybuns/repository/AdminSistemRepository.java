package com.developer.onlybuns.repository;


import com.developer.onlybuns.entity.AdminSistem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminSistemRepository extends JpaRepository<AdminSistem, Integer> {
    AdminSistem findByEmail(String email);
    AdminSistem findByEmailAndPassword(String email, String password);
    @Query("SELECT email FROM AdminSistem")
    List<String> findAllEmails();

}
