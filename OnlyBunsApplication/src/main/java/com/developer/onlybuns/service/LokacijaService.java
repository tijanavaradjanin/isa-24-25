package com.developer.onlybuns.service;

import com.developer.onlybuns.entity.Lajk;
import com.developer.onlybuns.entity.Lokacija;
import org.springframework.cache.annotation.CacheEvict;

public interface LokacijaService {

    void saveLokacija(Lokacija lokacija);

    String getAdresa(double latituda, double longituda);

    Lokacija findById(Integer id);

    @CacheEvict(value = "lokacijaCache", allEntries = true)
    void removeFromCache();
}
