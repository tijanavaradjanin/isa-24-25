package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.entity.Komentar;
import com.developer.onlybuns.repository.KomentarRepository;
import com.developer.onlybuns.service.KomentarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KomentarServiceImpl implements KomentarService {

    @Autowired
    public KomentarRepository komentarRepository;

    public KomentarServiceImpl(KomentarRepository komentarRepository) {
        this.komentarRepository = komentarRepository;
    }

    @Override
    public void saveKomentar(Komentar komentar){
        komentarRepository.save(komentar);
    }
}
