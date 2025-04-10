package com.developer.onlybuns.service;

import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendSummaryEmail(RegistrovaniKorisnik korisnik, String summary) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("tijana.varadjanin@gmail.com");
        message.setSubject("Vaša nedeljna statistika");
        message.setText(summary);
        mailSender.send(message);
    }
}
