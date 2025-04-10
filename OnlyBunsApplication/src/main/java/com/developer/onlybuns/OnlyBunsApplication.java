package com.developer.onlybuns;

import com.developer.onlybuns.repository.AdminSistemRepository;
import com.developer.onlybuns.repository.KorisnikRepository;
import com.developer.onlybuns.repository.ObjavaRepository;
import com.developer.onlybuns.repository.RegistrovaniKorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@SpringBootApplication
@EnableScheduling
public class OnlyBunsApplication {

	//@Autowired
	//private KorisnikRepository korisnikRepository;

	@Autowired
	private AdminSistemRepository adminSistemRepository;

	@Autowired
	private RegistrovaniKorisnikRepository registrovaniKorisnikRepository;

	@Autowired
	private ObjavaRepository objavaRepository;

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}

	public static void main(String[] args) {
		SpringApplication.run(OnlyBunsApplication.class, args);
	}

}
