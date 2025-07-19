package com.developer.onlybuns.service;
import com.developer.onlybuns.entity.Lajk;
import com.developer.onlybuns.entity.Objava;
import com.developer.onlybuns.entity.Pracenje;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.repository.LajkRepository;
import com.developer.onlybuns.repository.ObjavaRepository;
import com.developer.onlybuns.repository.PracenjeRepository;
import com.developer.onlybuns.repository.RegistrovaniKorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class InactiveUserNotificationService {

    @Autowired
    private RegistrovaniKorisnikRepository korisnikRepository;

    @Autowired
    private PracenjeRepository pracenjeRepository;

    @Autowired
    private LajkRepository lajkRepository;

    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private ObjavaRepository objavaRepository;

    @Scheduled(cron = "0 10 15 * * ?")     //sec min h * *, svaki dan u 3 i 10 popodne
    public void notifyInactiveUsers() {
        Timestamp sevenDaysAgo = Timestamp.valueOf(LocalDateTime.now().minusDays(7));
        List<RegistrovaniKorisnik> inactiveUsers = korisnikRepository.findByLastLoginBefore(sevenDaysAgo);

        for (RegistrovaniKorisnik korisnik : inactiveUsers) {
            String summary = generateUserSummary(korisnik);
            emailSenderService.sendSummaryEmail(korisnik, summary);
        }
        emailSenderService.sendSummaryEmail(new RegistrovaniKorisnik(), "Ovo je testni email.");
    }

    @PostConstruct
    public void testPokretanjeNakonStarta() {
        // pokreće se 5 minuta nakon što se aplikacija startuje
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                notifyInactiveUsers();
            }
        }, 5 * 60 * 1000); // 5 minuta u milisekundama
    }

    private String generateUserSummary(RegistrovaniKorisnik korisnik) {
        //filter za nove pratioce u poslednjih nedelju dana
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Pracenje> novaPracenja = pracenjeRepository.findByZapraceniIdAndDatumPracenja(korisnik.getId(), sevenDaysAgo);
        int brojNovihPratilaca = novaPracenja.size();

        //dodavanje svih lajkova u proteklih 7 dana u listu svih novih lajkova na objavama tog korisnika
        List<Objava> objave=objavaRepository.findByKorisnikId(korisnik.getId());
        List<Lajk> noviLajkovi= new ArrayList<>();
        for(Objava objava:objave){
            List<Lajk> lajkovi=lajkRepository.findByObjavaIdAndVremeLajkovanja(objava.getId(), sevenDaysAgo);
            noviLajkovi.addAll(lajkovi);
        }
        int brojNovihLajkova=noviLajkovi.size();

        //objave svih ljudi koje taj korisnik prati
        List<Pracenje> pracenja=pracenjeRepository.findByPracenjeId(korisnik.getId());
        List<Objava> noveObjave=new ArrayList<>();
        for(Pracenje pracenje:pracenja){
            List<Objava> objaveZapracenih=objavaRepository.findByKorisnikIdAndVremeKreiranja(pracenje.getZapraceni().getId(), sevenDaysAgo);
            noveObjave.addAll(objaveZapracenih);
        }
        int brojNovihObjava=noveObjave.size();

        return "Postovani korisnice " + korisnik.getKorisnickoIme() + ", u proteklih 7 dana dobili ste " + brojNovihPratilaca + " novih pratilaca, "
                + brojNovihLajkova + " novih lajkova i " + brojNovihObjava + " novih objava.";
    }
}

