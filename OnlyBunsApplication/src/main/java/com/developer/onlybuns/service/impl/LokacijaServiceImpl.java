package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.entity.Lajk;
import com.developer.onlybuns.entity.Lokacija;
import com.developer.onlybuns.repository.LajkRepository;
import com.developer.onlybuns.repository.LokacijaRepository;
import com.developer.onlybuns.service.LokacijaService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public String getAdresa(double latituda, double longituda) {
        try {
            String url = String.format(
                    "https://nominatim.openstreetmap.org/reverse?lat=%f&lon=%f&format=json",
                    latituda, longituda
            );

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "OnlyBunsApp");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JSONObject jsonObject = new JSONObject(response.getBody());
            if (jsonObject.has("display_name")) {
                return jsonObject.getString("display_name");
            } else {
                return "Nepoznata lokacija";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Greška pri dohvatanju adrese";
        }
    }

}
