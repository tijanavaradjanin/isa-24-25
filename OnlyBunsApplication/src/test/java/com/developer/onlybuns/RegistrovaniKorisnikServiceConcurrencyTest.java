package com.developer.onlybuns;

import com.developer.onlybuns.entity.Lokacija;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.repository.LokacijaRepository;
import com.developer.onlybuns.repository.RegistrovaniKorisnikRepository;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource(properties = {
        "spring.cache.jcache.config=classpath:ehcache-nopersist.xml"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RegistrovaniKorisnikServiceConcurrencyTest {

    @Autowired
    private RegistrovaniKorisnikService registrovaniKorisnikService;

    @Autowired
    private RegistrovaniKorisnikRepository registrovaniKorisnikRepository;

    @Autowired
    private LokacijaRepository lokacijaRepository;

    private final AtomicInteger successCount = new AtomicInteger();
    private final List<String> uspesnoRegistrovani = Collections.synchronizedList(new ArrayList<>());

    @Test
    public void testConcurrentRegistrationWithSameUsername() throws InterruptedException {
        String korisnickoIme = "duplikatKorisnik";
        String email1 = "korisnik1@example.com";
        String email2 = "korisnik2@example.com";
        String sifra = "sifra123";

        // ocisti prethodni korisnicki unos, ako postoji
        registrovaniKorisnikRepository.deleteByKorisnickoIme(korisnickoIme);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        // pokreni dva paralelna zadatka
        executor.submit(kreirajTask(email1, korisnickoIme, sifra, latch));
        executor.submit(kreirajTask(email2, korisnickoIme, sifra, latch));

        System.out.println("\n Pokrećem konkurentnu registraciju sa istim korisničkim imenom...\n");
        latch.countDown(); // pusti oba da krenu u isto vreme

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // ocekuje se da samo jedna registracija prodje
        Assertions.assertEquals(1, successCount.get(), "Samo jedan korisnik sme da bude uspešno registrovan!");
        Assertions.assertEquals(1, uspesnoRegistrovani.size(), "Tačno jedan korisnik treba biti uspešan.");

        System.out.println("\nKonačno uspešan korisnik: " + uspesnoRegistrovani.get(0));
    }

    private Runnable kreirajTask(String email, String korisnickoIme, String sifra, CountDownLatch latch) {
        return () -> {
            try {
                latch.await();

                RegistrovaniKorisnik korisnik = new RegistrovaniKorisnik();
                korisnik.setEmail(email);
                korisnik.setKorisnickoIme(korisnickoIme);
                korisnik.setPassword(sifra);
                korisnik.setIme("Ime");
                korisnik.setPrezime("Prezime");

                Lokacija lokacija = new Lokacija(45.0, 19.0);
                lokacija = lokacijaRepository.save(lokacija); // mora da postoji zbog non-null constraint
                korisnik.setLokacija(lokacija);
                korisnik.setBroj("123");
                korisnik.setInfo("Info");

                registrovaniKorisnikService.saveRegistrovaniKorisnik(korisnik);
                successCount.incrementAndGet();
                uspesnoRegistrovani.add(email);
                System.out.println("Uspešno registrovan: " + email);

            } catch (Exception e) {
                System.out.println("Neuspeh za: " + email + " → " + e.getMessage());
            }
        };
    }
}
