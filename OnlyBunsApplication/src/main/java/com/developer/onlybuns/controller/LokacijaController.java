package com.developer.onlybuns.controller;
import com.developer.onlybuns.entity.Lokacija;
import com.developer.onlybuns.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.cache.CacheManager;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/lokacija")
public class LokacijaController {

    private final LokacijaService lokacijaService;

    @Autowired
    private final CacheManager jCacheManager;

    public LokacijaController(LokacijaService lokacijaService, CacheManager jCacheManager) {
        this.lokacijaService=lokacijaService;
        this.jCacheManager=jCacheManager;
    }

    @GetMapping("/{id}")
    public  ResponseEntity<Lokacija> findLokacijaById(@PathVariable("id") Integer id) {
        Lokacija lokacija = lokacijaService.findById(id);
        if (lokacija != null) {
            return ResponseEntity.ok(lokacija);
        } else {
            return (ResponseEntity<Lokacija>) ResponseEntity.notFound();
        }
    }

    @PreAuthorize("hasAnyAuthority('KORISNIK', 'ADMIN')")
    @DeleteMapping(value = "/removeCache")
    public ResponseEntity<String> removeFromCache() {
        lokacijaService.removeFromCache();
        return ResponseEntity.ok("Lokacije removuspesno obrisane iz kesa!");
    }

    @PreAuthorize("hasAnyAuthority('KORISNIK', 'ADMIN')")
    @GetMapping("/checkCache")
    public ResponseEntity<?> testLokacijaCache() {
        javax.cache.Cache<Object, Object> cache = jCacheManager.getCache("lokacijaCache");
        List<Object> keys = new ArrayList<>();
        cache.forEach(entry -> keys.add(entry.getKey()));
        return ResponseEntity.ok(keys);
    }

    @PreAuthorize("hasAnyAuthority('KORISNIK', 'ADMIN')")
    @GetMapping("/checkAdresaCache")
    public ResponseEntity<?> testLokacijaAdresaCache() {
        javax.cache.Cache<Object, Object> cache = jCacheManager.getCache("lokacijaAdresaCache");
        List<Object> keys = new ArrayList<>();
        cache.forEach(entry -> keys.add(entry.getKey()));
        return ResponseEntity.ok(keys);
    }

}
