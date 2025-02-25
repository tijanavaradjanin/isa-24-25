package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.entity.Pracenje;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.repository.PracenjeRepository;
import com.developer.onlybuns.service.PracenjeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PracenjeServiceImpl implements PracenjeService {

    @Autowired
    public PracenjeRepository pracenjeRepository;

    public PracenjeServiceImpl(PracenjeRepository pracenjeRepository) {
        this.pracenjeRepository = pracenjeRepository;
    }

    @Override
    public void savePracenje(Pracenje pracenje) {
        pracenjeRepository.save(pracenje);
    }

    @Override
    public void removePracenje(Pracenje pracenje) {
        pracenjeRepository.delete(pracenje);
    }

    @Override
    public boolean proveriDaLiPrati(Integer pratilacId, Integer zapraceniId) {
        return pracenjeRepository.existsByPratilacIdAndZapraceniId(pratilacId, zapraceniId);
    }

    @Override
    public Pracenje pronadjiPracenje(Integer pratilacId, Integer zapraceniId){
        return pracenjeRepository.findByPratilacIdAndZapraceniId(pratilacId, zapraceniId);
    }


    @Override
    public List<RegistrovaniKorisnik> zapraceniKorisnici(Integer idKorisnika){
        System.out.println("Id koga trazimo u pracenjima: " + idKorisnika);
        List<Pracenje> pracenja=pracenjeRepository.findByPracenjeId(idKorisnika);
        System.out.println(pracenja);
        List<RegistrovaniKorisnik> zapraceni=new ArrayList<>();
        for(Pracenje p: pracenja) {
            zapraceni.add(p.getZapraceni());
        }
        return zapraceni;
    }
}
