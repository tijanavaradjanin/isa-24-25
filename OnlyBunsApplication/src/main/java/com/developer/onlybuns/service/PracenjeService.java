package com.developer.onlybuns.service;

import com.developer.onlybuns.entity.Pracenje;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;

import java.util.List;
import java.util.Optional;

public interface PracenjeService {

    void savePracenje(Pracenje pracenje);

    void removePracenje(Pracenje pracenje);

    Pracenje pronadjiPracenje(Integer pratilacId, Integer zapraceniId);

    List<RegistrovaniKorisnik> zapraceniKorisnici(Integer idKorisnika);

    boolean proveriDaLiPrati(Integer PratilacId, Integer ZapraceniId);
}
