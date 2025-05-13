package com.developer.onlybuns.service;

import com.developer.onlybuns.entity.Lajk;
import com.developer.onlybuns.entity.Lokacija;

public interface LokacijaService {

    void saveLokacija(Lokacija lokacija);

    String getAdresa(double latituda, double longituda);
}
