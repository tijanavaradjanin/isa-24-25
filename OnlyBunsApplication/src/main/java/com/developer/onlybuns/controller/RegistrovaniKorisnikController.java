package com.developer.onlybuns.controller;
import com.developer.onlybuns.dto.KorisnikProfilDTO;
import com.developer.onlybuns.dto.UpdateProfile;
import com.developer.onlybuns.dto.UserRequest;
import com.developer.onlybuns.entity.Korisnik;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.entity.Uloga;
import com.developer.onlybuns.entity.UserDetails;
import com.developer.onlybuns.service.ObjavaService;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import com.developer.onlybuns.util.TokenUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.*;
import org.json.JSONObject;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/registrovaniKorisnik")
public class RegistrovaniKorisnikController {

    private final RegistrovaniKorisnikService registrovaniKorisnikService;

    private final ObjavaService objavaService;

    @Autowired
    private TokenUtils tokenUtils;

    public RegistrovaniKorisnikController(RegistrovaniKorisnikService registrovaniKorisnikService, ObjavaService objavaService) {
        this.registrovaniKorisnikService = registrovaniKorisnikService;
        this.objavaService = objavaService;
    }

    @GetMapping
    public List<RegistrovaniKorisnik> findAllRegistrovaniKorisnik() {
        return registrovaniKorisnikService.findAllRegistrovaniKorisnik();
    }

    @GetMapping("/{id}")
    public Optional<RegistrovaniKorisnik> findRegistrovaniKorisnikById(@PathVariable("id") Integer id) {
        return registrovaniKorisnikService.findById(id);
    }

    //prikaz profila za admine koji mogu videti sifre
    @GetMapping("/korisnickoIme/{korisnickoIme}")
    public RegistrovaniKorisnik findRegistrovaniKorisnikByKorisnickoIme(@PathVariable("korisnickoIme") String korisnickoIme) {
        return registrovaniKorisnikService.findByKorisnickoIme(korisnickoIme);
    }

    //prikaz profila za neautentifikovane korisnike
    @GetMapping("/korisnikProfil/{korisnickoIme}")
    public ResponseEntity<KorisnikProfilDTO> findKorisnikProfilByKorisnickoIme(
            @PathVariable("korisnickoIme") String korisnickoIme) {

        RegistrovaniKorisnik korisnik = registrovaniKorisnikService.findByKorisnickoIme(korisnickoIme);

        if (korisnik == null) {
            return ResponseEntity.notFound().build();
        }

        KorisnikProfilDTO korisnikDTO = new KorisnikProfilDTO(
                korisnik.getKorisnickoIme(),
                korisnik.getIme(),
                korisnik.getPrezime(),
                korisnik.getGrad(),
                korisnik.getDrzava(),
                korisnik.getBroj(),
                korisnik.getEmail(),
                korisnik.getInfo()
        );

        return ResponseEntity.ok(korisnikDTO);
    }

    //prepoznaje principal-radi u postmanu
    @GetMapping("/profil")
    public ResponseEntity<RegistrovaniKorisnik> getUser(Principal principal) {
        String korisnickoIme = principal.getName();
        System.out.println("KORISNIK KOJI GLEDA SVOJ PROFIL JE: " + korisnickoIme);
        System.out.println("Principal sadrzi: " + principal.getName());
        RegistrovaniKorisnik user = registrovaniKorisnikService.findByKorisnickoIme(korisnickoIme);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/add")
    public ResponseEntity<?> saveRegistrovaniKorisnik(@Valid @RequestBody RegistrovaniKorisnik korisnikEntity) {
        //try catch blok po uzoru na prijaviKorisnika
        try {
            RegistrovaniKorisnik response = registrovaniKorisnikService.saveRegistrovaniKorisnik(korisnikEntity);
            return ResponseEntity.ok(response);

        }  //catch (IllegalArgumentException e) {
        // String jsonString = "{\"Error\":" + "\"" + e.getMessage() + "\"" + "}";
        // JSONObject json = new JSONObject(jsonString);
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(json);
        //  }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            // Vraćanje poruke konflikta sa detaljima
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserRequest userRequest, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
        } else {
            Optional<RegistrovaniKorisnik> existUserEmail = this.registrovaniKorisnikService.findByEmail(userRequest.getEmail());
            if (existUserEmail.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Profil sa tim mejlom vec postoji.");
            } else {
                RegistrovaniKorisnik existUserUsername = this.registrovaniKorisnikService.findByKorisnickoIme(userRequest.getKorisnickoIme());
                if (existUserUsername != null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Korisnicko ime je zauzeto.");
                } else {
                    if (!Objects.equals(userRequest.getPassword(), userRequest.getPotvrdaLozinke())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lozinka i njena potvrda se moraju poklapati.");
                    } else {
                        if (objavaService.validateLocation(userRequest.getGrad(), userRequest.getDrzava()) == null) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nevalidna lokacija.");
                        } else {
                            RegistrovaniKorisnik regKorisnik = new RegistrovaniKorisnik(
                                    userRequest.getEmail(),
                                    userRequest.getPassword(),
                                    userRequest.getIme(),
                                    userRequest.getPrezime(),
                                    userRequest.getKorisnickoIme(),
                                    userRequest.getGrad(),
                                    userRequest.getDrzava(),
                                    userRequest.getBroj(),
                                    userRequest.getInfo()
                            );
                            registrovaniKorisnikService.saveRegistrovaniKorisnik(regKorisnik);
                            return new ResponseEntity<>(regKorisnik, HttpStatus.CREATED);
                        }
                    }
                }
            }
        }
    }


    //pocetni login, bez tokena, za autentifikaciju koristi se login iz authcontrollera
    @PostMapping("/login")
    public ResponseEntity<String> prijaviKorisnika(@RequestBody RegistrovaniKorisnik korisnik) {
        RegistrovaniKorisnik userCredentials = registrovaniKorisnikService.proveriKredencijale(korisnik.getEmail(), korisnik.getPassword());
        if (userCredentials != null) {
            return ResponseEntity.ok("{\"message\": \"Prijava uspesna!\"}");
        } else {
            return ResponseEntity.status(401).body("Neispravan mejl ili lozinka.");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?>  updateProfile(@RequestBody @Valid UpdateProfile dto, Principal principal) {
        RegistrovaniKorisnik korisnik = registrovaniKorisnikService.findByKorisnickoIme(principal.getName());

        if (dto.getIme() != null) korisnik.setIme(dto.getIme());
        if (dto.getPrezime() != null) korisnik.setPrezime(dto.getPrezime());
        if (dto.getBroj() != null) korisnik.setBroj(dto.getBroj());
        if (dto.getInfo() != null) korisnik.setInfo(dto.getInfo());

        if (dto.getKorisnickoIme() != null && !dto.getKorisnickoIme().equals(korisnik.getKorisnickoIme())) {
            if (registrovaniKorisnikService.findByKorisnickoIme(dto.getKorisnickoIme()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Korisnicko ime je vec zauzeto.");
            }
            korisnik.setKorisnickoIme(dto.getKorisnickoIme());
        }

        if (dto.getGrad() != null && dto.getDrzava() != null) {
            // Ako menja oba (grad i državu), validiramo kombinaciju
            if (objavaService.validateLocation(dto.getGrad(), dto.getDrzava()) == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Nevalidna lokacija.");
            }
            korisnik.setGrad(dto.getGrad());
            korisnik.setDrzava(dto.getDrzava());
        } else if (dto.getGrad() != null) {
            // Ako menja samo grad, proveravamo da li pripada trenutnoj državi
            if (objavaService.validateLocation(dto.getGrad(), korisnik.getDrzava()) == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Izabrani grad ne pripada trenutnoj drzavi.");
            }
            korisnik.setGrad(dto.getGrad());
        } else if (dto.getDrzava() != null) {
            // Ako menja samo državu, proveravamo da li trenutni grad pripada novoj državi
            if (objavaService.validateLocation(korisnik.getGrad(), dto.getDrzava()) == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Trenutni grad ne pripada izabranoj novoj drzavi.");
            }
            korisnik.setDrzava(dto.getDrzava());
        }

        if (dto.getNovaLozinka() != null) {
            if (!dto.getPassword().equals(korisnik.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Stara lozinka nije tacna.");
            }
            if (!dto.getNovaLozinka().equals(dto.getPotvrdaNoveLozinke())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Nova lozinka i potvrda se ne poklapaju.");
            }
            korisnik.setPassword(dto.getNovaLozinka());
        }
        registrovaniKorisnikService.saveRegistrovaniKorisnik(korisnik);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Profil uspesno azuriran.");
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
