package com.developer.onlybuns;

import com.developer.onlybuns.repository.AdminSistemRepository;
import com.developer.onlybuns.repository.KorisnikRepository;
import com.developer.onlybuns.repository.ObjavaRepository;
import com.developer.onlybuns.repository.RegistrovaniKorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlyBunsApplication {

	//@Autowired
	//private KorisnikRepository korisnikRepository;

	@Autowired
	private AdminSistemRepository adminSistemRepository;

	@Autowired
	private RegistrovaniKorisnikRepository registrovaniKorisnikRepository;

	@Autowired
	private ObjavaRepository objavaRepository;

	public static void main(String[] args) {
		SpringApplication.run(OnlyBunsApplication.class, args);
	}

}
