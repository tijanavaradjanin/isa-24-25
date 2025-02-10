package com.developer.onlybuns.controller;
import com.developer.onlybuns.dto.ObjavaRequestDTO;
import com.developer.onlybuns.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.*;
import java.io.*;
import java.nio.file.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.developer.onlybuns.dto.ObjavaDTO;
import com.developer.onlybuns.entity.Komentar;
import com.developer.onlybuns.entity.Lajk;
import com.developer.onlybuns.entity.Objava;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.service.ObjavaService;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/objava")
public class ObjavaController {

    private final ObjavaService objavaService;

    private final RegistrovaniKorisnikService registrovaniKorisnikService;

    @Autowired
    TokenUtils tokenUtils;

    public ObjavaController(ObjavaService objavaService, RegistrovaniKorisnikService registrovaniKorisnikService) {
        this.objavaService = objavaService;
        this.registrovaniKorisnikService = registrovaniKorisnikService;
    }
/*
    @PostMapping("/add")
    public ResponseEntity<String> save(@RequestBody ObjavaDTO objavaDTO) {  //proveri je li okej preko objavaDTO a bez novaobjavaDTO
        Optional<RegistrovaniKorisnik> registrovaniKorisnik = registrovaniKorisnikService.findByKorisnickoIme(objavaDTO.getKorisnickoIme());
        if (registrovaniKorisnik != null) {
            List<Komentar> komentari = new ArrayList<Komentar>();
            List<Lajk> lajkovi = new ArrayList<Lajk>();
            Objava objava = new Objava(objavaDTO.getOpis(), objavaDTO.getSlika(), objavaDTO.getLatituda(), objavaDTO.getLongituda(), objavaDTO.getVremeKreiranja(), registrovaniKorisnik.get(), komentari, lajkovi);
            objavaService.saveObjava(objava);

            return ResponseEntity.ok("{\"message\": \"Uspesno ste kreirali objavu!\"}");
        } else {
            return ResponseEntity.status(401).body("Zao nam je, kreiranje objave nije uspelo, pokusajte ponovo sa ispravnim unosom.");
        }

    }
*/

   /* @PostMapping("/add")
    public ResponseEntity<?> save(
            @RequestParam("opis") String opis,
            @RequestParam("slika") MultipartFile slika,
            @RequestParam("latituda") Double latituda,
            @RequestParam("longituda") Double longituda,
            @RequestParam("korisnickoIme") String korisnickoIme) {

        try {
            // Kreiranje direktorijuma ako ne postoji
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generišemo jedinstveno ime za fajl
            String fileName = UUID.randomUUID().toString() + "_" + slika.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // Zapisivanje slike na fajl sistem
            slika.transferTo(filePath);

            // Sačuvaj putanju do slike u bazi (ovde samo vraćamo putanju, u stvarnom slučaju treba koristiti Repository)
            String slikaPutanja = filePath.toString();

            // Kreiraj i sačuvaj objavu u bazi
            // Pretpostavljamo da koristimo neki repository za upis u bazu
            ObjavaDTO objava = new ObjavaDTO();
            objava.setOpis(opis);
            objava.setSlika(slikaPutanja);  // Putanja do slike se čuva
            objava.setLatituda(latituda);
            objava.setLongituda(longituda);
            RegistrovaniKorisnik registrovaniKorisnik = registrovaniKorisnikService.findByKorisnickoIme(korisnickoIme);

            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
           // System.out.println(vremeKreiranja); //string
           // LocalDateTime modifikacija=LocalDateTime.parse(vremeKreiranja, formatter);
            //System.out.println(modifikacija);
           // System.out.println(LocalDateTime.now());
           // OffsetDateTime odt=OffsetDateTime.parse(vremeKreiranja, formatter);
            //String vremeModifikovano = vremeKreiranja.replace ( " " , "T" );
            objava.setVremeKreiranja(LocalDateTime.now());


            List<Komentar> komentari = new ArrayList<Komentar>();
            List<Lajk> lajkovi = new ArrayList<Lajk>();
            Objava novaObjava=new Objava(objava.getOpis(), objava.getSlika(), objava.getLatituda(), objava.getLongituda(), objava.getVremeKreiranja(), registrovaniKorisnik, komentari, lajkovi);
            objavaService.saveObjava(novaObjava);
            System.out.println(novaObjava);

            return ResponseEntity.ok(novaObjava);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greška pri čuvanju slike: " + e.getMessage());
        }
    }
    */

/*
    @GetMapping("/mojeObjave")
    public ResponseEntity<Optional<Objava>> getMYPosts(@RequestParam String korisnickoIme) {
        Optional<RegistrovaniKorisnik> vlasnik=registrovaniKorisnikService.findByKorisnickoIme(korisnickoIme);
        Integer vlasnikId=vlasnik.get().getId();
        Optional<Objava> mojeObjave = objavaService.findByKorisnikId(vlasnikId);
        return ResponseEntity.ok(mojeObjave);
    }
    */

/*
    @GetMapping("/mojeObjave")
    public ResponseEntity<List<Objava>> getMyPosts(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
            }
            String token = authorizationHeader.substring(7);
            String korisnickoIme = tokenUtils.getUsernameFromToken(token);

            if (korisnickoIme == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            RegistrovaniKorisnik korisnik = registrovaniKorisnikService.findByKorisnickoIme(korisnickoIme);
            if (korisnik == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            System.out.println("Primljen zahtev za korisnika: " + korisnickoIme);
            RegistrovaniKorisnik vlasnik = registrovaniKorisnikService.findByKorisnickoIme(korisnickoIme);
            if (vlasnik == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Integer vlasnikId = vlasnik.getId();
            List<Objava> mojeObjave = objavaService.findByKorisnikId(vlasnikId);
            return ResponseEntity.ok(mojeObjave);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    */

   /* @PostMapping("/add")
    public ResponseEntity<?> save(
            @RequestBody ObjavaRequestDTO objavaRequestDTO,
            Principal principal) {

        try {
            // Proveri validnost lokacije
            double[] koordinate = objavaService.validateLocation(objavaRequestDTO.getGrad(), objavaRequestDTO.getDrzava());
            if (koordinate == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Greška: Nevalidna lokacija! Molimo unesite ispravan grad i državu.");
            }

            double latituda = koordinate[0];
            double longituda = koordinate[1];
            String slikaPutanja="";

            // Kreiranje direktorijuma ako ne postoji
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

             if(objavaRequestDTO.getSlika() != null && !objavaRequestDTO.getSlika().isEmpty()) {
                 slikaPutanja = "uploads/" + objavaRequestDTO.getSlika();
             }

            // Kreiranje i čuvanje objave
            String korisnickoIme = principal.getName();
            System.out.println("KORISNIK KOJI PRAVI OBJAVU JE: " + korisnickoIme);
            RegistrovaniKorisnik registrovaniKorisnik = registrovaniKorisnikService.findByKorisnickoIme(korisnickoIme);
            Objava novaObjava = new Objava(objavaRequestDTO.getOpis(), slikaPutanja, latituda, longituda, LocalDateTime.now(), registrovaniKorisnik, new ArrayList<>(), new ArrayList<>());

            objavaService.saveObjava(novaObjava);
            return ResponseEntity.ok(novaObjava);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greška pri čuvanju slike: " + e.getMessage());
        }
    }
    */

    @GetMapping("/sveobjave")
    public ResponseEntity<List<Objava>> getSveObjave() {
        List<Objava> objave = objavaService.getAllObjave();
        return ResponseEntity.ok(objave);
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(
            @RequestParam("opis") String opis,
            @RequestParam(required = false) String grad,
            @RequestParam(required = false) String drzava,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestPart("slika") MultipartFile slikaFile,
            Principal principal) {

        try {
            double[] koordinate = objavaService.validateLocation(grad, drzava);
            if (koordinate == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Nevalidna lokacija! Molimo unesite ispravan grad i državu.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            if ((grad == null || grad.isEmpty()) && (drzava == null || drzava.isEmpty()) && (latitude == null || longitude == null)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Morate uneti ili grad i državu ili odabrati lokaciju na mapi."));
            }

            double latituda = koordinate[0];
            double longituda = koordinate[1];

            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = slikaFile.getOriginalFilename();
            String filePath = "uploads/" + originalFilename;
            Path fileStoragePath = uploadPath.resolve(originalFilename);
            Files.copy(slikaFile.getInputStream(), fileStoragePath, StandardCopyOption.REPLACE_EXISTING);

            RegistrovaniKorisnik registrovaniKorisnik = registrovaniKorisnikService.findByKorisnickoIme(principal.getName());
            Objava novaObjava = new Objava(opis, filePath, latituda, longituda, LocalDateTime.now(), registrovaniKorisnik, new ArrayList<>(), new ArrayList<>());

            objavaService.saveObjava(novaObjava);
            return ResponseEntity.ok(novaObjava);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greška pri čuvanju slike: " + e.getMessage());
        }
    }

    @GetMapping("/mojeObjave")
    public ResponseEntity<List<Objava>> getMyPosts(Principal principal) {

            System.out.println("Primljen zahtev za korisnika: " + principal.getName());

            RegistrovaniKorisnik vlasnik = registrovaniKorisnikService.findByKorisnickoIme(principal.getName());
            if (vlasnik == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Integer vlasnikId = vlasnik.getId();
            List<Objava> mojeObjave = objavaService.findByKorisnikId(vlasnikId);
            return ResponseEntity.ok(mojeObjave);
    }

    @PutMapping
    public Objava update(@RequestBody Objava objava) {
        return objavaService.updateObjava(objava);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        objavaService.deleteObjava(id);
    }

    @GetMapping("/lajkovi")
    public ResponseEntity<List<Lajk>> getAllLajkovi(Integer id) {
        List<Lajk> lajkovi = objavaService.getAllLajkovi(id);
        return ResponseEntity.ok(lajkovi);
    }

    @GetMapping("/komentari")
    public ResponseEntity<List<Komentar>> getAllKomentari(Integer id) {
        List<Komentar> komentari = objavaService.getAllKomentari(id);
        return ResponseEntity.ok(komentari);
    }

    @GetMapping("/obliznjeObjave")
    public ResponseEntity<List<ObjavaDTO>> findNearbyPosts(Principal principal) {
        RegistrovaniKorisnik korisnik =  registrovaniKorisnikService.findByKorisnickoIme(principal.getName());
        List<ObjavaDTO> nearbyPosts = objavaService.findNearbyPosts(korisnik);

        if (nearbyPosts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(nearbyPosts);
    }



























}
