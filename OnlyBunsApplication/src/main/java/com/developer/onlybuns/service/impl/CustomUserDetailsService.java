/*package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.entity.Korisnik;
import com.developer.onlybuns.repository.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.developer.onlybuns.entity.Korisnik;
import com.developer.onlybuns.repository.KorisnikRepository;

import java.util.Optional;

// Ovaj servis je namerno izdvojen kao poseban u ovom primeru.
// U opstem slucaju UserServiceImpl klasa bi mogla da implementira UserDetailService interfejs.
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private KorisnikRepository korisnikRepository;

	// Funkcija koja na osnovu username-a iz baze vraca objekat User-a
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Korisnik> korisnik = korisnikRepository.findByKorisnickoIme(username);
		if (korisnik == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			return (UserDetails) korisnik;
		}
	}

}*/