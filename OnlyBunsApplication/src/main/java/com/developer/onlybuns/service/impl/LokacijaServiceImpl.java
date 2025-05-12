package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.entity.Lajk;
import com.developer.onlybuns.entity.Lokacija;
import com.developer.onlybuns.repository.LajkRepository;
import com.developer.onlybuns.repository.LokacijaRepository;
import com.developer.onlybuns.service.LokacijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LokacijaServiceImpl implements LokacijaService {

    @Autowired
    public LokacijaRepository lokacijaRepository;

    public LokacijaServiceImpl(LokacijaRepository lokacijaRepository) {
        this.lokacijaRepository = lokacijaRepository;
    }

    @Override
    public void saveLokacija(Lokacija lokacija){
        lokacijaRepository.save(lokacija);
    }
}
