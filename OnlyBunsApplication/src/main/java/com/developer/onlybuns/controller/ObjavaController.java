package com.developer.onlybuns.controller;
import com.developer.onlybuns.dto.KomentarDTO;
import com.developer.onlybuns.dto.LajkDTO;
import com.developer.onlybuns.entity.*;
import com.developer.onlybuns.service.*;
import com.developer.onlybuns.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

import com.developer.onlybuns.dto.ObjavaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/objava")
public class ObjavaController {

    private final ObjavaService objavaService;

    private final PracenjeService pracenjeService;

    private final LajkService lajkService;

    private final KomentarService komentarService;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    private RateLimiterService rateLimiterService;

    public ObjavaController(ObjavaService objavaService, PracenjeService pracenjeService, LajkService lajkService, KomentarService komentarService) {
        this.objavaService = objavaService;
        this.pracenjeService=pracenjeService;
        this.lajkService=lajkService;
        this.komentarService=komentarService;
    }

    @PreAuthorize("hasAnyAuthority('KORISNIK', 'ADMIN')")
    @GetMapping("/sveobjave")
    public ResponseEntity<List<ObjavaDTO>> getSveObjave() {
        List<Objava> objave = objavaService.getAllObjave();
        List<ObjavaDTO> objavePrikaz=new ArrayList<>();
        for(Objava objava: objave){
            objavePrikaz.add(new ObjavaDTO(
                    objava.getId(),
                    objava.getOpis(),
                    objava.getSlika(),
                    objava.getLatituda(),
                    objava.getLongituda(),
                    objava.getVremeKreiranja(),
                    objava.getKomentari(),
                    objava.getLajkovi(),
                    objava.getRegistrovaniKorisnik().getKorisnickoIme(),
                    objava.getLajkovi().size(),
                    objava.getKomentari().size()
            ));
        }
        return ResponseEntity.ok(objavePrikaz);
    }

    @PreAuthorize("hasAuthority('KORISNIK')")
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(
            @RequestParam("opis") String opis,
            @RequestParam(required = false) String grad,
            @RequestParam(required = false) String drzava,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestPart("slika") MultipartFile slikaFile,
            Authentication authentication) {

        try {
            // Provera da li su koordinate prosleđene sa fronta
            double latituda;
            double longituda;

            if (latitude != null && longitude != null) {
                // Ako su latitude i longitude prosleđeni, koristi ih
                latituda = latitude;
                longituda = longitude;
            } else {
                // Ako nisu, validiraj na osnovu grad/država
                double[] koordinate = objavaService.validateLocation(grad, drzava);
                if (koordinate == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "Nevalidna lokacija! Molimo unesite ispravan grad i državu."));
                }
                latituda = koordinate[0];
                longituda = koordinate[1];
            }

            // Loguj vrednosti da proveriš da li su dobre
            System.out.println("Latitude: " + latituda + ", Longitude: " + longituda);

            // Čuvanje slike
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = slikaFile.getOriginalFilename();
            String filePath = "uploads/" + originalFilename;
            Path fileStoragePath = uploadPath.resolve(originalFilename);
            Files.copy(slikaFile.getInputStream(), fileStoragePath, StandardCopyOption.REPLACE_EXISTING);

            // Kreiranje objave
            RegistrovaniKorisnik user=(RegistrovaniKorisnik)authentication.getPrincipal();
            Objava novaObjava = new Objava(opis, filePath, latituda, longituda, LocalDateTime.now(), user, new ArrayList<>(), new ArrayList<>());

            objavaService.saveObjava(novaObjava);
            return ResponseEntity.ok(novaObjava);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greška pri čuvanju slike: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('KORISNIK')")
    @GetMapping("/mojeObjave")
    public ResponseEntity<List<Objava>> getMyPosts(Authentication authentication) {

            RegistrovaniKorisnik vlasnik = (RegistrovaniKorisnik) authentication.getPrincipal();
            if (vlasnik == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Integer vlasnikId = vlasnik.getId();
            List<Objava> mojeObjave = objavaService.findByKorisnikId(vlasnikId);
            Collections.sort(mojeObjave, (o1, o2) -> o2.getVremeKreiranja().compareTo(o1.getVremeKreiranja()));
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

    @PreAuthorize("hasAuthority('KORISNIK')")
    @PostMapping("/lajkuj")
    public ResponseEntity<?> likeAPost(@RequestParam("objavaId") Integer objavaId, Authentication authentication) {
        RegistrovaniKorisnik korisnik = (RegistrovaniKorisnik) authentication.getPrincipal();
        Objava objava = objavaService.getById(objavaId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objava nije pronađena"));
        boolean prati = pracenjeService.proveriDaLiPrati(korisnik.getId(), objava.getRegistrovaniKorisnik().getId());
        if (!prati) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Ne mozete lajkovati ovu objavu jer ne pratite korisnika.");
        }
        Lajk lajk = new Lajk(korisnik, objava, LocalDateTime.now());
        lajkService.saveLajk(lajk);

        return ResponseEntity.ok("Objava lajkovana uspesno!");
    }

    @PreAuthorize("hasAuthority('KORISNIK')")
    @PostMapping("/komentarisi")
    public ResponseEntity<?> commentAPost(@RequestParam("objavaId") Integer objavaId, @RequestParam("sadrzaj") String sadrzaj, Authentication authentication) {
        RegistrovaniKorisnik korisnik = (RegistrovaniKorisnik) authentication.getPrincipal();
        if (!rateLimiterService.isAllowed(korisnik.getId())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Previse zahteva u minuti. Pokusajte ponovo kasnije.");
        } else {
            Objava objava = objavaService.getById(objavaId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objava nije pronađena"));
            //ispod zakomentarisano to da proverava da li prati korisnika
            /*boolean prati = pracenjeService.proveriDaLiPrati(korisnik.getId(), objava.getRegistrovaniKorisnik().getId());
            if (!prati) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Ne mozete komentarisati ovu objavu jer ne pratite korisnika.");
            }*/
            Komentar komentar = new Komentar(sadrzaj, LocalDateTime.now(), korisnik, objava);
            komentarService.saveKomentar(komentar);

            return ResponseEntity.ok("Objava komentarisana uspesno!");
        }
    }

    @PreAuthorize("hasAnyAuthority('KORISNIK', 'ADMIN')")
    @GetMapping("/komentari")
    public ResponseEntity<List<KomentarDTO>> seeComments(@RequestParam("objavaId") Integer objavaId) {
        Objava objava = objavaService.getById(objavaId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objava nije pronađena"));
        List<Komentar> komentariObjave = new ArrayList<>();
        komentariObjave = objavaService.getAllKomentari(objavaId);
        List<KomentarDTO> komentariObjaveDTO= new ArrayList<>();
        for(Komentar kom : komentariObjave){
            komentariObjaveDTO.add(new KomentarDTO(
                    kom.getId(),
                    kom.getSadrzaj(),
                    kom.getRegistrovaniKorisnik().getKorisnickoIme(),
                    kom.getVremeKomentarisanja(),
                    kom.getObjava().getId()
            ));
        }
        Collections.sort(komentariObjaveDTO, (o1, o2) -> o2.getVremeKomentarisanja().compareTo(o1.getVremeKomentarisanja()));
        return ResponseEntity.ok(komentariObjaveDTO);
    }

    @PreAuthorize("hasAnyAuthority('KORISNIK', 'ADMIN')")
    @GetMapping("/lajkovi")
    public ResponseEntity<List<LajkDTO>> seeLikes(@RequestParam("objavaId") Integer objavaId) {
        Objava objava = objavaService.getById(objavaId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objava nije pronađena"));
        List<Lajk> lajkoviObjave = new ArrayList<>();
        lajkoviObjave = objavaService.getAllLajkovi(objavaId);
        List<LajkDTO> lajkoviObjaveDTO= new ArrayList<>();
        for(Lajk lajk : lajkoviObjave){
            lajkoviObjaveDTO.add(new LajkDTO(
                    lajk.getId(),
                    lajk.getRegistrovaniKorisnik().getKorisnickoIme(),
                    lajk.getVremeLajkovanja(),
                    lajk.getObjava().getId()
            ));
        }
        Collections.sort(lajkoviObjaveDTO, (o1, o2) -> o2.getVremeLajkovanja().compareTo(o1.getVremeLajkovanja()));
        return ResponseEntity.ok(lajkoviObjaveDTO);
    }

    @PreAuthorize("hasAuthority('KORISNIK')")
    @GetMapping("/obliznjeObjave")
    public ResponseEntity<List<ObjavaDTO>> findNearbyPosts(Authentication authentication) {
        RegistrovaniKorisnik korisnik = (RegistrovaniKorisnik) authentication.getPrincipal();
        List<ObjavaDTO> nearbyPosts = objavaService.findNearbyPosts(korisnik);

        if (nearbyPosts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(nearbyPosts);
    }

    @PreAuthorize("hasAuthority('KORISNIK')")
    @GetMapping("/feed")
    public ResponseEntity<List<ObjavaDTO>> MyFeed(Authentication authentication) {
        RegistrovaniKorisnik registrovaniKorisnik = (RegistrovaniKorisnik) authentication.getPrincipal();
        System.out.println("Ulogovan je korisnik sa id-jem: " + registrovaniKorisnik.getId());
        List<RegistrovaniKorisnik> zapraceniKorisnici=pracenjeService.zapraceniKorisnici(registrovaniKorisnik.getId());
        System.out.println("Ovaj korisnik prati korisnike: " + zapraceniKorisnici);
        List<Objava> objaveFeed=new ArrayList<>();
        for(RegistrovaniKorisnik rk:zapraceniKorisnici) {
            objaveFeed.addAll(objavaService.findByKorisnikId(rk.getId()));
        }
        Collections.sort(objaveFeed, (o1, o2) -> o2.getVremeKreiranja().compareTo(o1.getVremeKreiranja()));
        List<ObjavaDTO> objaveFeed2=new ArrayList<>();
        for(Objava objava:objaveFeed) {
            objaveFeed2.add(new ObjavaDTO(
                    objava.getId(),
                    objava.getOpis(),
                    objava.getSlika(),
                    objava.getLatituda(),
                    objava.getLongituda(),
                    objava.getVremeKreiranja(),
                    objava.getKomentari(),
                    objava.getLajkovi(),
                    objava.getRegistrovaniKorisnik().getKorisnickoIme(),
                    objava.getLajkovi().size(),
                    objava.getKomentari().size()
            ));
        }
        return ResponseEntity.ok(objaveFeed2);
    }
}
